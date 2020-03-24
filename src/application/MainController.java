package application;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import view.Pdi;

public class MainController {
	
	@FXML ImageView imageView1;
	@FXML ImageView imageView2;
	@FXML ImageView imageView3;
	
	@FXML Label lblR;
	@FXML Label lblG;
	@FXML Label lblB;
	
	private Image img1;
	private Image img2;
	private Image img3;
	
	@FXML TextField pcr;
	@FXML TextField pcg;
	@FXML TextField pcb;
	
	@FXML RadioButton v3x3;
	@FXML RadioButton vX;
	@FXML RadioButton vCruz;
	
	@FXML Slider limiar;
	
	@FXML
	public void abreImagem1() {
		img1 = abreImagem(imageView1, img1);
	}
	
	@FXML
	public void abreImagem2() {
		img2 = abreImagem(imageView2, img2);
	}
	
	private void atualizaImagem3() {
		imageView3.setImage(img3);
		imageView3.setFitHeight(img3.getHeight());
		imageView3.setFitWidth(img3.getWidth());
	}
	
	private Image abreImagem(ImageView imageView, Image image) {
		
		File f = selecionaImagem();
		if(f != null) {
			image = new Image(f.toURI().toString());
			imageView.setImage(image);
			imageView.setFitHeight(image.getHeight());
			imageView.setFitWidth(image.getWidth());
			
			return image;
		}
		return null;
	}
	
	
	private File selecionaImagem() {
		   FileChooser fileChooser = new FileChooser();
		   fileChooser.getExtensionFilters().add(new 
				   FileChooser.ExtensionFilter(
						   "Imagens", "*.jpg", "*.JPG", 
						   "*.png", "*.PNG", "*.gif", "*.GIF", 
						   "*.bmp", "*.BMP")); 	
		   fileChooser.setInitialDirectory(new File("C:\\Users\\Matheus\\Pictures\\imgs pdi"));
		   File imgSelec = fileChooser.showOpenDialog(null);
		   try {
			   if (imgSelec != null) {
			    return imgSelec;
			   }
		   } catch (Exception e) {
			e.printStackTrace();
		   }
		   return null;
	}
	
	
	private void verificaCor(Image img, int x, int y){
		  try {
				Color cor = img.getPixelReader().getColor(x-1, y-1);
				lblR.setText("R: "+(int) (cor.getRed()*255));
				lblG.setText("G: "+(int) (cor.getGreen()*255));
				lblB.setText("B: "+(int) (cor.getBlue()*255));
			} catch (Exception e) {
				//e.printStackTrace();
			}
	  }
	
	 @FXML
	 public void rasterImg(MouseEvent evt){
		 ImageView iw = (ImageView)evt.getTarget();
		 if(iw.getImage()!=null)
			 verificaCor(iw.getImage(), (int)evt.getX(), (int)evt.getY());
	  }
	 
	 @FXML
	 public void cinzaAritmetica() {
		 img3 = Pdi.cinzaMediaAritmetica(img1, 0, 0, 0);
		 atualizaImagem3();
	 }
	 
	 @FXML
	 public void cinzaPonderada() {
		 int r = Integer.parseInt(pcr.getText());
		 int g = Integer.parseInt(pcg.getText());
		 int b = Integer.parseInt(pcb.getText());
		 img3 = Pdi.cinzaMediaAritmetica(img1, r, g, b);
		 atualizaImagem3();
	 }
	 
	 @FXML	 	 
	 public void limiariza() {
		 img3 = Pdi.limiarizacao(img1, limiar.getValue()/255);
		 atualizaImagem3();
	 }
	 
	 @FXML	 	 
	 public void negativa() {
		 img3 = Pdi.negativa(img1);
		 atualizaImagem3();
	 }
	 
	 @FXML
	 public void eliminaRuidos() {
		 if(v3x3.isSelected()) {
			 img3 = Pdi.ruidos(img1, 1);
			 atualizaImagem3();
		 }else if(vCruz.isSelected()) {
			 img3 = Pdi.ruidos(img1, 2);
			 atualizaImagem3();
		 }else if(vX.isSelected()) {
			 img3 = Pdi.ruidos(img1, 3);
			 atualizaImagem3();
		 }
	 }
	
}
