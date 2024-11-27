package org.telegrambot.demobot.common;

/**
 * @author alikhandani
 * @created 26/11/2024
 * @project englishday
 */
public class GptCostCalculator {

    public static double costCalculator(ChatGPTModel gptModel, int input , int output){
        return gptModel.getInputCostPerToken() * input + gptModel.getOutputCostPerToken() * output;
    }
}
