package filter;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.ServiceUrlConstants;
import org.keycloak.util.KeycloakUriBuilder;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String baseUrl = getBaseUrl(request);
		URI logoutUri = KeycloakUriBuilder.fromUri("/auth").path(ServiceUrlConstants.TOKEN_SERVICE_LOGOUT_PATH)
				.queryParam("client_id","SistCoopApp")
				.queryParam("redirect_uri", "/SistCoopApp/index.caja.html")
				.queryParam("login", true)
				.queryParam("state", "26%2F41f70fc8-aaae-47a5-a845-231c87b6fc24")
				.build("SistemaFinanciero");	
				
		//response.setContentType("text/html");
		response.sendRedirect(logoutUri.toString());
		//response.sendRedirect(logoutUri.toString());

		/*
		 * Client client = ClientBuilder.newClient(); WebTarget target =
		 * client.target(logoutUri); Response res = target.request().get();
		 * String value = res.readEntity(String.class); res.close(); // You
		 * should close connections!
		 */
	}

	public static String getBaseUrl(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		return url.substring(0, url.indexOf('/', 8));
	}

}