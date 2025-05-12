package it.polimi.ingsw.gc11.controller.action.server;

import it.polimi.ingsw.gc11.controller.GameContext;

public class RewardDecisionAction extends ClientAction {
    private final boolean decision;

    public RewardDecisionAction(String username, boolean decision) {
        super(username);
        this.decision = decision;
    }

    @Override
    public void execute(GameContext ctx) {
        ctx.rewardDecision(getUsername(), decision);
    }
}
