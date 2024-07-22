package com.mca.pdfgen.beans;

import lombok.Data;

@Data
public class AuthToken
{
    private long tokenTime;
    private String cachedToken;
    
    public void clear()
    {
        tokenTime = 0;
        cachedToken = null;
    }
}
