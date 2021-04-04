import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.Scanner;

public class eieio extends Application {
	public static void main (String[] args) {
		launch(args);
	}

	public void start (Stage stage) {

		BorderPane root = new BorderPane();
		Insets insets = new Insets(20, 20, 20, 20);
		root.setPadding(insets);
		TextArea input = new TextArea("Enter your text here!");
		input.setEditable(true);
		input.setWrapText(true);
		input.setMinWidth(200);
		root.setCenter(input);

		TextArea output = new TextArea();
		output.setEditable(false);
		output.setMaxWidth(320);
		output.setWrapText(true);
		root.setRight(output);

		VBox checks = new VBox();
		checks.setSpacing(10);
		CheckBox escapeCheck = new CheckBox("Add escape character \"\\\"");
		escapeCheck.setSelected(true);
		CheckBox addBackTicks = new CheckBox("Add Triple Backticks");
		CheckBox ignoreUpperCheck = new CheckBox("Ignore Upper Case Vowels");
		checks.getChildren().addAll(escapeCheck, addBackTicks, ignoreUpperCheck);
		root.setTop(checks);

		HBox buttonPane = new HBox();
		Button censorButton = new Button("Censor!");
		Button toClipboard = new Button("Copy to Clipboard");
		buttonPane.getChildren().addAll(censorButton, toClipboard);
		root.setBottom(buttonPane);
		buttonPane.setSpacing(20);
		censorButton.setOnAction((event) -> {
			Scanner scan = new Scanner(input.getText());
			scan.useDelimiter("[\\s]");
			ArrayList<String> words = new ArrayList<>();
			while (scan.hasNext()) {
				words.add(scan.next());
			}
			String vowels = "";
			for (int i = 0; i < words.size(); i++) {
				String word = words.get(i);
				for (int e = 0; e < word.length(); e++) {
					char character = word.charAt(e);
					if (character == 'a' || character == 'e' ||
						character == 'i' || character == 'o' ||
						character == 'u' ||
						(!ignoreUpperCheck.isSelected() &&
						 (character == 'A' || character == 'E' ||
						  character == 'I' || character == 'O' ||
						  character == 'U'))
					) {
						vowels += character;
						if (ignoreUpperCheck.isSelected()) {
							if (escapeCheck.isSelected()) {
								words.set(i, word.replaceFirst("[aeiou]", "\\\\*"));
							} else {
								words.set(i, word.replaceFirst("[aeiou]", "*"));
							}
						} else {
							if (escapeCheck.isSelected()) {
								words.set(i, word.replaceFirst("[aeiouAEIOU]", "\\\\*"));
							} else {
								words.set(i, word.replaceFirst("[aeiouAEIOU]", "*"));
							}
						}
						break;
					}
				}
			}
			String out = "";
			if (addBackTicks.isSelected()) {
				out += "```\n";
			}
			for (String word : words) {
				out += word + " ";
			}
			if (escapeCheck.isSelected()) {
				out += "\n\n\\*" + vowels;
			} else {
				out += "\n\n*" + vowels;
			}
			if (addBackTicks.isSelected()) {
				out += "\n```";
			}
			output.setText(out);
		});


		toClipboard.setOnAction((event) -> {
			ClipboardContent content = new ClipboardContent();
			content.putString(output.getText());
			Clipboard.getSystemClipboard().setContent(content);
		});

		BorderPane.setMargin(input, insets);
		Scene scene = new Scene(root, 800, 600);
		stage.setTitle("EIEIO Censor Generator");
		stage.setScene(scene);
		stage.show();
	}

}