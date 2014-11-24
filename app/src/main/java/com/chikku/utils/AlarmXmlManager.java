package com.chikku.utils;

import android.media.MediaMuxer;
import android.util.Log;

import com.pojo.Alarm;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by chikku on 16/11/14.
 */
public class AlarmXmlManager {

    private static String ALARMS_STORAGE_FILE_PATH;
    private static String ALARMS_FOLDER_PATH;


    public static void init(String folderPath) {
        ALARMS_FOLDER_PATH = folderPath;
        ALARMS_STORAGE_FILE_PATH = "Alarms.xml";
    }

    public static void flushAlarmManager() {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        new File(AlarmXmlManager.ALARMS_FOLDER_PATH + "/" + AlarmXmlManager.ALARMS_STORAGE_FILE_PATH).delete();

        Document document = null;
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            document = documentBuilder.newDocument();

        } catch (Exception e) {
            Log.i("DocumentParser", "Exception caught:" + e);
        }

        AlarmManager manager = AlarmManager.getInstance();

        Element rootElement = document.createElement("Alarms");

        for (int i = 0; i < manager.getNumberOfAlarms(); i++) {
            Element element = getAlarmAsEvent(manager.getAlarm(i), document);
            rootElement.appendChild(element);
            Log.d("XmlManager", "Added child:" + i);
        }

        document.appendChild(rootElement);

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);

            File xmlFile = new File(AlarmXmlManager.ALARMS_FOLDER_PATH + "/" + AlarmXmlManager.ALARMS_STORAGE_FILE_PATH);
//            Log.d("XmlManager", "File Path:" + AlarmXmlManager.ALARMS_FOLDER_PATH + "/" + AlarmXmlManager.ALARMS_STORAGE_FILE_PATH);
            StreamResult stream = new StreamResult(new FileOutputStream(xmlFile));

            transformer.transform(source, stream);

        } catch (Exception e) {

        }
    }

    private static Element getAlarmAsEvent(Alarm alarm, Document document) {
        Element element = document.createElement("Alarm");
        element.setAttribute("Name", alarm.getName());

        Element hours = document.createElement("Hours");
        Element minutes = document.createElement("Minutes");
        Element status = document.createElement("Status");
        Element defaultApp = document.createElement("DefaultApplication");
        defaultApp.setAttribute("Name" , alarm.getLaunchAppDetails().getApplicationName());

        Text hoursText = document.createTextNode(Integer.toString(alarm.getHour()));
        Text minutesText = document.createTextNode(Integer.toString(alarm.getMinutes()));
        Text defaultAppPackageName = document.createTextNode(alarm.getLaunchAppDetails().getPackageName());

        Text statusText = null;
        if (alarm.isEnabled()) {
            statusText = document.createTextNode("On");
        } else {
            statusText = document.createTextNode("Off");
        }


        hours.appendChild(hoursText);
        minutes.appendChild(minutesText);
        status.appendChild(statusText);
        defaultApp.appendChild(defaultAppPackageName);

        element.appendChild(hours);
        element.appendChild(minutes);
        element.appendChild(status);
        element.appendChild(defaultApp);


        return element;
    }

    public static void loadAlarmManager() {
//        Log.d("XmlManager", "In loadAlarmManager");
        Document document = null;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            File xmlFile = new File(AlarmXmlManager.ALARMS_FOLDER_PATH + "/" + AlarmXmlManager.ALARMS_STORAGE_FILE_PATH);
            if (xmlFile.exists()) {
                Log.w("XmlManager", "Xml file present");
                document = documentBuilder.parse(xmlFile);
            } else {
                Log.w("XmlManager", "Xml file absent");
                return;
            }


        } catch (Exception e) {
            Log.w("XmlManager", "Exception caught" + e.toString());
        }

        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("Alarm");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element alarmElement = (Element) node;

//                Log.d("XmlManager", "Name:" + alarmElement.getAttribute("Name"));
//                Log.d("XmlManager", "Hours" + alarmElement.getElementsByTagName("Hours").item(0).getTextContent());
//                Log.d("XmlManager" , "Minutes" + alarmElement.getElementsByTagName("Minutes").item(0).getTextContent());
//                Log.d("XmlManager", "Status" + alarmElement.getElementsByTagName("Status").item(0).getTextContent());

                Alarm alarm = new Alarm();
                alarm.setName(alarmElement.getAttribute("Name"));
                alarm.setHour(Integer.parseInt(alarmElement.getElementsByTagName("Hours").item(0).getTextContent()));
                alarm.setMinutes(Integer.parseInt(alarmElement.getElementsByTagName("Minutes").item(0).getTextContent()));

                Element defaultAppElement = (Element)alarmElement.getElementsByTagName("DefaultApplication").item(0);
                alarm.getLaunchAppDetails().setApplicationName(defaultAppElement.getAttribute("Name"));
                alarm.getLaunchAppDetails().setPackageName(defaultAppElement.getTextContent());

                String status = alarmElement.getElementsByTagName("Status").item(0).getTextContent().toString();
                Log.d("XmlManager", "Status:" + alarmElement.getElementsByTagName("Status").item(0).getTextContent());
                if (status .equals("On")) {
                    Log.d("XmlManager", "Status set to true");
                    alarm.setEnabled(true);
                } else {
                    alarm.setEnabled(false);
                }

                alarm.setAlarmTimeHour(alarm.getHour());
                alarm.setAlarmMinute(alarm.getMinutes());

                AlarmManager.getInstance().addAlarm(alarm);
            }
        }

    }
}
