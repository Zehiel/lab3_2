package edu.iis.mto.staticmock;

public class IncomingInfo {

    private final String content;
    private final SubsciptionType subscriptionType;

    public IncomingInfo(String content, SubsciptionType subscriptionType) {
        this.content = content;
        this.subscriptionType = subscriptionType;
    }

    public boolean requiresSubsciption() {
        return subscriptionType != SubsciptionType.NONE;
    }

    public String getContent() {
        return content;
    }

    public SubsciptionType getSubscriptionType() {
        return subscriptionType;
    }

}
