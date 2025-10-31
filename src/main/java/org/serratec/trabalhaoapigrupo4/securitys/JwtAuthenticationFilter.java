

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serratec.ecommerce.dtos.LoginDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/auth/login"); // define o endpoint de autenticação
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            // 🔹 Lê o corpo JSON e converte para DTO
            ObjectMapper mapper = new ObjectMapper();
            LoginDTO login = mapper.readValue(request.getInputStream(), LoginDTO.class);

            System.out.println("Tentando autenticar usuário: " + login.getEmail());

            // 🔹 Cria o token de autenticação (sem authorities, serão resolvidas depois)
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());

            // 🔹 Envia para o AuthenticationManager validar (via UserDetailsService)
            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler credenciais de login: " + e.getMessage(), e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        // 🔹 Obtém o username autenticado
        String username = ((UserDetails) authResult.getPrincipal()).getUsername();

        // 🔹 Gera o token JWT
        String token = jwtUtil.generateToken(username);

        // 🔹 Retorna no header e também no corpo JSON
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("access-control-expose-headers", "Authorization");
        response.setContentType("application/json");

        String jsonResponse = String.format("{\"token\":\"%s\",\"type\":\"Bearer\",\"user\":\"%s\"}", token, username);
        response.getWriter().write(jsonResponse);

        System.out.println(" Login bem-sucedido! Token JWT gerado para: " + username);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {

        // 🔹 Retorno amigável em caso de falha no login
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Usuário ou senha inválidos\"}");

        System.out.println(" Falha na autenticação: " + failed.getMessage());
    }
}
