package org.telegrambot.demobot.common;

/**
 * @author alikhandani
 * @created 26/11/2024
 * @project englishday
 */
public enum ChatGPTModel {
    // GPT-4 Models
    GPT_4("gpt-4", 0.00003, 0.00006),
    GPT_4_32K("gpt-4-32k", 0.00006, 0.00012),
    GPT_4_TURBO("gpt-4-turbo", 0.00001, 0.00003),
    GPT_4O("gpt-4o", 0.000005, 0.000015),
    GPT_4O_MINI("gpt-4o-mini", 0.0000003, 0.0000012),

    // GPT-3.5 Models
    GPT_3_5_TURBO("gpt-3.5-turbo", 0.000003, 0.000006),
    GPT_3_5_TURBO_16K("gpt-3.5-turbo-16k", 0.000003, 0.000004),

    // Legacy Models
    DAVINCI_002("davinci-002", 0.000002, 0.000002),
    BABBAGE_002("babbage-002", 0.0000004, 0.0000004);

    private final double inputCostPerToken;
    private final double outputCostPerToken;
    private final String apiName;

    ChatGPTModel(String apiName, double inputCostPerToken, double outputCostPerToken) {
        this.apiName = apiName;
        this.inputCostPerToken = inputCostPerToken;
        this.outputCostPerToken = outputCostPerToken;
    }
    public String getApiName(){
        return apiName;
    }

    public double getInputCostPerToken() {
        return inputCostPerToken;
    }

    public double getOutputCostPerToken() {
        return outputCostPerToken;
    }

    @Override
    public String toString() {
        return String.format("Model: %s, Input Cost: $%.10f, Output Cost: $%.10f",
                name(), inputCostPerToken, outputCostPerToken);
    }
}