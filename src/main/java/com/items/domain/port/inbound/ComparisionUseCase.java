package com.items.domain.port.inbound;


import com.items.domain.model.ComparisionResult;


public interface ComparisionUseCase  { 
    ComparisionResult compare(String id1, String id2);
 
    
}
