package filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.keycloak.constants.ServiceUrlConstants;
import org.keycloak.util.KeycloakUriBuilder;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    StringBuffer requestUrl = request.getRequestURL();
        int indexBase = requestUrl.indexOf(request.getServletContext().getContextPath());

        String logoutUri = KeycloakUriBuilder.fromUri("/auth")
                .path(ServiceUrlConstants.TOKEN_SERVICE_LOGOUT_PATH)
                .queryParam("redirect_uri", requestUrl.substring(0, indexBase) + "/" + Configuration.appName)
                .build(Configuration.realName).toString();

        response.setContentType("text/html");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(response.encodeRedirectURL(logoutUri));
	}

	public static String getBaseUrl(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		return url.substring(0, url.indexOf('/', 8));
	}

}