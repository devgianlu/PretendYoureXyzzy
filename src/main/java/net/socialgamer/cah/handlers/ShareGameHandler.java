package net.socialgamer.cah.handlers;

import net.socialgamer.cah.Constants;
import net.socialgamer.cah.RequestWrapper;
import net.socialgamer.cah.data.Game;
import net.socialgamer.cah.data.GameManager;
import net.socialgamer.cah.data.User;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gianlu
 */
public class ShareGameHandler extends GameWithPlayerHandler {

  public static final String OP = Constants.AjaxOperation.SHARE_GAME.toString();

  @Inject
  public ShareGameHandler(GameManager gameManager) {
    super(gameManager);
  }

  @Override
  public Map<Constants.ReturnableData, Object> handleWithUserInGame(RequestWrapper request, HttpSession session, User user, Game game) {
    Map<Constants.ReturnableData, Object> ret = new HashMap<>();
    ret.put(Constants.AjaxResponse.GAME_SHARE_ID, game.getShareId());
    return ret;
  }
}
