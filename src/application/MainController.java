package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Pixel;
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
	@FXML Slider ti1;
	@FXML Slider ti2;
	
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
	 
	 @FXML
	 public void adiciona() {
		 img3 = Pdi.adicao(img1, img2, ti1.getValue()/255, ti2.getValue()/255);
		 atualizaImagem3();
	 }
	 
	 @FXML
	 public void subtrai() {
		 img3 = Pdi.subtracao(img1, img2);
		 atualizaImagem3();
	 }
	
	 @FXML
	 public void salvar() {
		 if(img3 != null) {
			 FileChooser fileChooser = new FileChooser();
			 fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagem", "*.png"));
			 fileChooser.setInitialDirectory(new File("C:\\Users\\Matheus\\Pictures\\imgs pdi"));
			 File file = fileChooser.showSaveDialog(null);
			 if(file != null) {
				 BufferedImage bImg = SwingFXUtils.fromFXImage(img3, null);
				 try {
					 ImageIO.write(bImg, "PNG", file);
				 } catch (IOException e) {
					 e.printStackTrace();
				 }
			 }
		 }
	 }
	 
	 int x1;
	 int y1;
	 int x2;
	 int y2;
	 Pixel pInicio;
	 Pixel pFinal;
	 
	 @FXML
	 public void marcarEntrada(MouseEvent evento) {
	 	ImageView iv = (ImageView)evento.getTarget();
	 	if(iv.getImage()!=null) {
	 		 x1 = (int)evento.getX();
	 		 y1 = (int)evento.getY();
	 		 pInicio = verificaPixel(img1, x1, y1);
	 	}
	 	
	 }
	 
	 @FXML 	
	 public void marcarSaida(MouseEvent evento) {
 	 	ImageView iv = (ImageView)evento.getTarget();
	 	if(iv.getImage()!=null) {
	 		 x2 = (int)evento.getX();
	 		 y2 = (int)evento.getY();
	 		 pFinal = verificaPixel(img1, x2, y2);
	 		 iv.setImage(Pdi.marcacao(img1, pInicio, pFinal));
	 	}
	 	
	 }
	 
	 public Pixel verificaPixel(Image img, int x, int y){
		  try {
				Color cor = img.getPixelReader().getColor(x-1, y-1);
				Pixel p = new Pixel(cor.getRed()*255, cor.getGreen()*255, cor.getBlue()*255, x, y);
				return p;
			} catch (Exception e) {
				//e.printStackTrace();
				return null;
			}
	  }
	 
	 public void abreHistograma(ActionEvent event) {
		 try {
			 Stage stage = new Stage();
			 FXMLLoader loader = new FXMLLoader(getClass().getResource("Histograma.fxml"));
			 Parent root = loader.load();
			 stage.setScene(new Scene(root));
			 stage.setTitle("Histograma");			 
			 stage.initOwner(((Node) event.getSource()).getScene().getWindow());
			 stage.show();
			 
			 HistogramaController controller = (HistogramaController)loader.getController();
			 
			 if(img1!=null) {
				 Pdi.montaGrafico(img1, controller.grafico1);
			 }
			 if(img2!=null) {
				 Pdi.montaGrafico(img2, controller.grafico2);
			 }
			 if(img3!=null) {
				 Pdi.montaGrafico(img3, controller.grafico3);
			 }
			 
			 
		 } catch(Exception e) {
			 e.printStackTrace();
		 }
	 }
	 
}
