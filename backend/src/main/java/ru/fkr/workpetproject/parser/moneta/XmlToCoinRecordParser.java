package ru.fkr.workpetproject.parser.moneta;

import ru.fkr.workpetproject.dao.dto.moneta.MonetaRecordDto;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class XmlToCoinRecordParser {

    public static void parse(InputStream xmlInputStream, Consumer<List<MonetaRecordDto>> batchConsumer, int batchSize) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(xmlInputStream);

        List<MonetaRecordDto> batch = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "REP".equals(reader.getLocalName())) {
                MonetaRecordDto record = parseRep(reader);
                batch.add(record);

                if (batch.size() >= batchSize) {
                    batchConsumer.accept(new ArrayList<>(batch));
                    batch.clear();
                }
            }
        }

        if (!batch.isEmpty()) {
            batchConsumer.accept(batch);
        }

        reader.close();
    }

    private static MonetaRecordDto parseRep(XMLStreamReader reader) {
        return new MonetaRecordDto(
                reader.getAttributeValue(null, "PERIOD_ACC"),
                reader.getAttributeValue(null, "REP_ACC"),
                reader.getAttributeValue(null, "ELS"),
                parseDouble(reader.getAttributeValue(null, "SQ_PAY")),
                reader.getAttributeValue(null, "PREMISE_GUID"),
                reader.getAttributeValue(null, "FLAT"),
                null, // roomGuid
                null, // room
                reader.getAttributeValue(null, "OBJECT_ADDRESS"),
                reader.getAttributeValue(null, "OBJECT_GUID"),
                parseInt(reader.getAttributeValue(null, "LS_TYPE")),
                parseInt(reader.getAttributeValue(null, "LS_ID")),
                null, null, null, null, null, null
        );
    }

    private static Double parseDouble(String val) {
        try {
            return val != null ? Double.parseDouble(val) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer parseInt(String val) {
        try {
            return val != null ? Integer.parseInt(val) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
