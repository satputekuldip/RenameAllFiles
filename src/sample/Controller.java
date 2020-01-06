package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Window;

import java.io.File;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    public TextField et_folder_path;

    @FXML
    public TextField et_prefix;

    @FXML
    public TextField et_suffix;

    @FXML
    public TextField et_extension;

    @FXML
    public ComboBox<AlgorithmModel> combo_algorithm;

    @FXML
    public Button btn_rename_all;

    String folder_path = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        List<AlgorithmModel> algorithmList = new ArrayList<AlgorithmModel>();
        algorithmList.add(new AlgorithmModel(1, "MD5 HASH"));
        algorithmList.add(new AlgorithmModel(2, "Time Stamp"));
        algorithmList.add(new AlgorithmModel(3, "Counter"));

        combo_algorithm.getItems().addAll(FXCollections
                .observableArrayList(algorithmList));

        combo_algorithm.setOnAction(event -> System.out.println(combo_algorithm.getSelectionModel().getSelectedItem().getId()));

        btn_rename_all.setOnAction(event -> {
            Window owner = btn_rename_all.getScene().getWindow();

            folder_path = et_folder_path.getText().trim();

            String prefix = et_prefix.getText().trim();
            String suffix = et_suffix.getText().trim();


            if (et_folder_path.getText().isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Please enter Folder Path !");
                return;
            } else if (combo_algorithm.getValue() == null) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Please Select Algorithm !");
                return;
            } else {
                // creating new folder
                File myfolder = new File(folder_path);

                File[] file_array = myfolder.listFiles();
                if (file_array != null && file_array.length > 0) {
                    for (int i = 0; i < file_array.length; i++) {

                        if (file_array[i].isFile()) {

                            File myfile = new File(folder_path +
                                    "\\" + file_array[i].getName());
                            String long_file_name = file_array[i].getName();

                            System.out.println(long_file_name);

                            // file name format: "Snapshot 11 (12-05-2017 11-57).png"
                            // To Shorten it to "11.png", get the substring which
                            // starts after the first space character in the long
                            // _file_name.
                            String newName = long_file_name;
                            switch (combo_algorithm.getSelectionModel().getSelectedItem().getId()){
                                case 1: newName = md5Java(long_file_name);
                                    break;
                                case 2: newName = new SimpleDateFormat("YYYYMMddhhmmss", Locale.getDefault()).format(new Date()) + i;
                                    break;
                                case 3: newName = String.valueOf(i);
                                    break;
                                default:
                                    break;
                            }
                            String extension = et_extension.getText().trim().isEmpty() ? getFileExtension(long_file_name) : et_extension.getText().trim();
                            myfile.renameTo(new File(folder_path +
                                    "\\" + prefix + newName + suffix + (extension.startsWith(".") ? extension : "." + extension)));
                        }
                    }
                }
            }
        });
    }

    private String md5Java(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(message.getBytes("UTF-8"));

            //converting byte array to Hexadecimal String
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }

            digest = sb.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return digest;
    }

    private static String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}
