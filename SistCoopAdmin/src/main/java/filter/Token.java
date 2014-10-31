package filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.KeycloakSecurityContext;

/**
 * Servlet implementation class Token
 */
@WebServlet("/token")
public class Token extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		KeycloakSecurityContext session = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
		String tokenId = session.getTokenString();

		response.setHeader("Content-Type", "text/plain");
		response.setHeader("success", "yes");
		PrintWriter writer = response.getWriter();
		writer.write(tokenId);
		writer.close();
	}
}
