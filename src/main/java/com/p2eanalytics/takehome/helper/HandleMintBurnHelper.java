package com.p2eanalytics.takehome.helper;

import com.p2eanalytics.takehome.model.BurnRate;
import com.p2eanalytics.takehome.model.MintRate;
import lombok.Data;

@Data
public class HandleMintBurnHelper {
    MintRate mint;
    BurnRate burn;
}
