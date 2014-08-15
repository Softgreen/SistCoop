package filter;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.keycloak.ServiceUrlConstants;
import org.keycloak.util.KeycloakUriBuilder;

/**
 * Servlet implementation class ManageAccount
 */
@WebServlet("/ManageAccount")
public class ManageAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ManageAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		URI logoutUri = KeycloakUriBuilder.fromUri(getBaseUrl(request) + "/auth").path(ServiceUrlConstants.ACCOUNT_SERVICE_PATH).queryParam("referrer", "SistCoopApp").build("SistemaFinanciero");
		/*Client client = ClientBuilder.newClient();
		WebTarget target = client.target(logoutUri);
		Response res = target.request().get();
		String value = res.readEntity(String.class);
		res.close(); // You should close connections!*/
		response.setContentType("text/html");		
		response.sendRedirect(logoutUri.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	public static String getBaseUrl(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		return url.substring(0, url.indexOf('/', 8));
	}

}
