package mat.unical.it.dotsandboxes.view;

import java.util.Map;
import mat.unical.it.dotsandboxes.model.Player;

public interface PlayersStateView {
    void setCurrentPlayer(Player player);

    void setPlayerOccupyingBoxesCount(Map<Player, Integer> player_occupyingBoxesCount_map);

    void setWinner(Player winner);
}
