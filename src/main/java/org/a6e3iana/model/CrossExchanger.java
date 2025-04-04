package org.a6e3iana.model;

import jakarta.servlet.ServletException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class CrossExchanger {
    private int fromPairBaseId;
    private int fromPairTargetId;
    private double fromPairRate;
    private int toPairBaseId;
    private int toPairTargetId;
    private double toPairRate;
    private double crossRate;

    public void calculateCrossRate (int fromId, int toId) throws ServletException{
        if (this.fromPairBaseId == fromId){
            if(this.toPairBaseId == toId){
                this.crossRate = fromPairRate / toPairRate;
            } else if(this.toPairTargetId == toId){
                this.crossRate = fromPairRate * toPairRate;
            }
        } else if(this.fromPairTargetId == fromId){
            if(this.toPairBaseId == toId){
                this.crossRate = 1.0 / (fromPairRate * toPairRate);
            } else if(this.toPairTargetId == toId){
                this.crossRate = toPairRate / fromPairRate;
            }
        }
    }
}
