package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {

    private static final String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

    static String fileName = "data.xml";

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        List<Employee> staff = parseXML(columnMapping, fileName);

        System.out.println("Из XML-файла data.xml создано экземпляров класса Employee: " + staff.size()
                + "\n\nЭкземпляры класса:");
        staff.forEach(System.out::println);

        // Объекты класса Employee конвертируем в формат JSON и записываем в файл data.json
        String json = listToJson(staff);
        writeString(json, "data2.json");

        System.out.println();
        System.out.println("Все объекты класса Employee конвертированы в объекты JSON\n" +
                "в файле data2.json, расположенном в корневой папке проекта");
    }

    public static List<Employee> parseXML(String[] column, String fileName) throws ParserConfigurationException, IOException, SAXException {

        String[] attr = new String[columnMapping.length];
        List<Employee> staff = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileName));

        // Получение списка всех элементов employee внутри корневого элемента
        NodeList employee = doc.getDocumentElement().getElementsByTagName("employee");

        for (int i = 0; i < employee.getLength(); i++) {

            Element empNode = (Element) employee.item(i);
            for(int j = 0; j < column.length; j++) {
                NodeList emp = empNode.getElementsByTagName(column[j]);
                attr[j] = emp.item(0).getTextContent(); // значение
            }
            Employee stf = new Employee(attr);
            staff.add(stf);
        }
        return staff;
    }

    public static String listToJson(List<Employee> list) {

        GsonBuilder bld = new GsonBuilder();
        Gson gson = bld
                .setPrettyPrinting()
                .create();
        return gson.toJson(list);
    }

    public static void writeString(String json, String fileName) {

        try (FileWriter file = new FileWriter(fileName)) {

            file.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}