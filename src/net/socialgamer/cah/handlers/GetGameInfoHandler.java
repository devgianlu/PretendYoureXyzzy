package net.socialgamer.cah.handlers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.socialgamer.cah.Constants.AjaxOperation;
import net.socialgamer.cah.Constants.AjaxRequest;
import net.socialgamer.cah.Constants.AjaxResponse;
import net.socialgamer.cah.Constants.ErrorCode;
import net.socialgamer.cah.Constants.ReturnableData;
import net.socialgamer.cah.Constants.SessionAttribute;
import net.socialgamer.cah.RequestWrapper;
import net.socialgamer.cah.data.Game;
import net.socialgamer.cah.data.GameManager;
import net.socialgamer.cah.data.User;

import com.google.inject.Inject;


public class GetGameInfoHandler extends Handler {

  public static final String OP = AjaxOperation.GET_GAME_INFO.toString();

  private final GameManager gameManager;

  @Inject
  public GetGameInfoHandler(final GameManager gameManager) {
    this.gameManager = gameManager;
  }

  @Override
  public Map<ReturnableData, Object> handle(final RequestWrapper request, final HttpSession session) {
    final Map<ReturnableData, Object> data = new HashMap<ReturnableData, Object>();

    final User user = (User) session.getAttribute(SessionAttribute.USER);
    assert (user != null);

    final int gameId;

    if (request.getParameter(AjaxRequest.GAME_ID) == null) {
      return error(ErrorCode.NO_GAME_SPECIFIED);
    }
    try {
      gameId = Integer.parseInt(request.getParameter(AjaxRequest.GAME_ID));
    } catch (final NumberFormatException nfe) {
      return error(ErrorCode.INVALID_GAME);
    }

    final Game game = gameManager.getGame(gameId);
    if (game == null) {
      return error(ErrorCode.INVALID_GAME);
    }

    assert game.getId() == gameId : "Got a game with id not what we asked for.";

    data.put(AjaxResponse.GAME_INFO, game.getInfo());
    data.put(AjaxResponse.PLAYER_INFO, game.getPlayerInfo());

    return data;
  }

}