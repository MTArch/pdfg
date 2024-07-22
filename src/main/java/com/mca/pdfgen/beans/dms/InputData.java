package com.mca.pdfgen.beans.dms;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InputData
{
    @JsonProperty("NGOConnectCabinet_Input") 
    public final NGOConnectCabinetInput nGOConnectCabinet_Input = new NGOConnectCabinetInput();
}
