package filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.constants.ServiceUrlConstants;
import org.keycloak.util.KeycloakUriBuilder;

/**
 * Servlet implementation class ManageAccount
 */
@WebServlet("/account")
public class ManageAccount extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public ManageAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		KeycloakSecurityContext session = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
		String a = session.getTokenString();
		System.out.println("tokennnnn:" + a);
		String acctUri = KeycloakUriBuilder.fromUri("/auth").path(ServiceUrlConstants.ACCOUNT_SERVICE_PATH).queryParam("referrer", Configuration.appName).build(Configuration.realName).toString();
		response.sendRedirect(acctUri);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	public static String getBaseUrl(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		return url.substring(0, url.indexOf('/', 8));
	}

}
