

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.serratec.ecommerce.dtos.EnderecoDTO;
import com.serratec.ecommerce.entitys.Endereco;
import com.serratec.ecommerce.repositorys.EnderecoRepository;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/";

    public EnderecoDTO buscarPorCep(String cep) {

        // 🔹 1. Remove traços e espaços (normalização)
        String cepLimpo = cep.replaceAll("[^0-9]", "");

        // 🔹 2. Verifica se já existe no banco
        Optional<Endereco> enderecoOpt = enderecoRepository.findByCep(cepLimpo);
        if (enderecoOpt.isPresent()) {
            return new EnderecoDTO(enderecoOpt.get());
        }

        // 🔹 3. Caso não exista, consulta a API ViaCEP
        RestTemplate restTemplate = new RestTemplate();
        String uri = VIA_CEP_URL + cepLimpo + "/json/";

        Endereco enderecoApi = restTemplate.getForObject(uri, Endereco.class);

        // 🔹 4. Validação do retorno da API
        if (enderecoApi == null || enderecoApi.getCep() == null) {
            throw new RuntimeException("CEP não encontrado ou inválido.");
        }

        // 🔹 5. Ajusta o formato e salva
        enderecoApi.setCep(enderecoApi.getCep().replaceAll("-", ""));
        Endereco enderecoSalvo = enderecoRepository.save(enderecoApi);

        // 🔹 6. Retorna o DTO atualizado
        return new EnderecoDTO(enderecoSalvo);
    }
}
