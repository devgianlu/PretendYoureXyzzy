/**
 * Copyright (c) 2012-2018, Andy Janata
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * <p>
 * * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided
 * with the distribution.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.socialgamer.cah.servlets;

import net.socialgamer.cah.Constants.*;
import net.socialgamer.cah.RequestWrapper;
import net.socialgamer.cah.data.User;
import net.socialgamer.cah.handlers.Handler;
import net.socialgamer.cah.handlers.Handlers;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


/**
 * Servlet implementation class AjaxServlet.
 * <p>
 * This servlet is only used for client actions, not for long-polling.
 *
 * @author Andy Janata (ajanata@socialgamer.net)
 */
@WebServlet("/AjaxServlet")
public class AjaxServlet extends CahServlet {
  private static final long serialVersionUID = 1L;

  /**
   * @see CahServlet#doPost(HttpServletRequest, HttpServletResponse)
   */
  @Override
  protected void handleRequest(final HttpServletRequest request,
                               final HttpServletResponse response, final HttpSession hSession) throws
          IOException {
    final PrintWriter out = response.getWriter();
    final User user = (User) hSession.getAttribute(SessionAttribute.USER);
    if (null != user) {
      user.userDidSomething();
    }
    int serial = -1;
    if (request.getParameter(AjaxRequest.SERIAL.toString()) != null) {
      try {
        serial = Integer.parseInt(request.getParameter(AjaxRequest.SERIAL.toString()));
      } catch (final NumberFormatException nfe) {
        returnError(out, ErrorCode.BAD_REQUEST, -1);
        return;
      }
    }

    final String op = request.getParameter(AjaxRequest.OP.toString());
    if (op == null || op.equals("")) {
      returnError(out, ErrorCode.OP_NOT_SPECIFIED, serial);
      return;
    }

    final Handler handler;
    try {
      handler = getInjector().getInstance(Handlers.LIST.get(op));
    } catch (final Exception e) {
      log((User) hSession.getAttribute(SessionAttribute.USER), "Exception handling op " + op + ": "
              + e.toString());
      returnError(out, ErrorCode.BAD_OP, serial);
      return;
    }
    final Map<ReturnableData, Object> data = handler.handle(new RequestWrapper(request), hSession);
    data.put(AjaxResponse.SERIAL, serial);
    returnData(out, data);
  }
}
