package io.pivotal.poc.cs;

import java.util.List;

public interface ReaderService {
    List<DataMessage> receive(String recipient);

    void readMap();
}
