package view;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import model.Pixel;

public class Pdi {

	public static Image cinzaMediaAritmetica(Image imagem, int pcr, int pcg, int pcb) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w,h);
			PixelWriter pw = wi.getPixelWriter();
			
			double media;
			
			for(int i=0; i<w; i++) {
				for(int j=0; j<h; j++) {
					Color corA = pr.getColor(i,j);
					if(pcr != 0 || pcg != 0 || pcb != 0) {
						media = (corA.getRed()*pcr + corA.getGreen()*pcg +corA.getBlue()*pcb)/100;
					} else { 
						media = (corA.getRed()+corA.getGreen()+corA.getBlue())/3;
					}
					Color corN = new Color(media, media, media, corA.getOpacity());
					pw.setColor(i, j, corN);
				}
			}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image limiarizacao(Image imagem, double limiar) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					Color corA = pr.getColor(i, j);
					Color corN;
					
					if(corA.getRed() >= limiar) {
						corN = new Color(1,1,1,corA.getOpacity());
					} else {
						corN = new Color(0,0,0,corA.getOpacity());
					}
					pw.setColor(i, j, corN);
				}
			}
			
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image negativa(Image imagem) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();

			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					Color corA = pr.getColor(i, j);
					Color corN = new Color (1 - corA.getRed(),
											1 - corA.getGreen(),
											1 - corA.getBlue(),
											corA.getOpacity());
					pw.setColor(i, j, corN);
				}
			}
			
			return wi;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//tipo vizinho definido por um int: 1 = vizinhos 3x3; 2 = vizinhos em cruz; 3 = vizinhos em x
	public static Image ruidos(Image imagem, int tipoVizinho) {
		try {
			int w = (int)imagem.getWidth();
			int h = (int)imagem.getHeight();
			
			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			for (int i = 1; i < w-1; i++) {
				for (int j = 1; j < h-1; j++) {
					Color corA = pr.getColor(i, j);
					Pixel p = new Pixel(corA.getRed(), corA.getGreen(), corA.getBlue(), i, j);
					buscaVizinhos(imagem, p);
					Pixel vetor[] = null;
					if(tipoVizinho == 1) {
						vetor = p.v3;
					}
					if(tipoVizinho == 2) {
						vetor = p.vC;
					}
					if(tipoVizinho == 3) {
						vetor = p.vX;
					}
					double red = mediana(vetor,1);
					double green = mediana(vetor,2);
					double blue = mediana(vetor,3);
					
					Color corN = new Color(red,green,blue,corA.getOpacity());
					pw.setColor(i, j, corN);
					
				}
			}
			
		return wi;
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void buscaVizinhos(Image imagem, Pixel p) {
		p.vX = buscaVizinhosX(imagem,p);
		p.vC = buscaVizinhosC(imagem,p);
		p.v3 = buscaVizinhos3(imagem,p);
	}
	
	public static Pixel[] buscaVizinhosX(Image imagem, Pixel p) {
		Pixel[] vX = new Pixel[5];
		PixelReader pr = imagem.getPixelReader();
		
		Color cor = pr.getColor(p.x-1, p.y+1);
		vX[0] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y+1);
		
		cor = pr.getColor(p.x+1, p.y-1);
		vX[1] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y-1);
		
		cor = pr.getColor(p.x-1, p.y-1);
		vX[2] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y-1);
		
		cor = pr.getColor(p.x+1, p.y+1);
		vX[3] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y+1);
		vX[4] = p;
		
		return vX;
	}
	
	public static Pixel[] buscaVizinhosC(Image imagem, Pixel p) {
		Pixel[] vC = new Pixel[5];
		PixelReader pr = imagem.getPixelReader();
		
		Color cor = pr.getColor(p.x, p.y+1);
		vC[0] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x,p.y+1);
		
		cor = pr.getColor(p.x, p.y-1);
		vC[1] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x,p.y-1);
		
		cor = pr.getColor(p.x-1, p.y);
		vC[2] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y);
		
		cor = pr.getColor(p.x+1, p.y);
		vC[3] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y);
		vC[4] = p;
		
		return vC;
	}
	
	public static Pixel[] buscaVizinhos3(Image imagem, Pixel p) {
		Pixel[] v3 = new Pixel[9];
		PixelReader pr = imagem.getPixelReader();
		
		Color cor = pr.getColor(p.x, p.y+1);
		v3[0] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x,p.y+1);
		
		cor = pr.getColor(p.x, p.y-1);
		v3[1] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x,p.y-1);
		
		cor = pr.getColor(p.x-1, p.y);
		v3[2] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y);
		
		cor = pr.getColor(p.x+1, p.y);
		v3[3] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y);
		
		cor = pr.getColor(p.x-1, p.y+1);
		v3[4] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y+1);
		
		cor = pr.getColor(p.x+1, p.y-1);
		v3[5] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y-1);
		
		cor = pr.getColor(p.x-1, p.y-1);
		v3[6] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x-1,p.y-1);
		
		cor = pr.getColor(p.x+1, p.y+1);
		v3[7] = new Pixel(cor.getRed(),cor.getGreen(),cor.getBlue(),p.x+1,p.y+1);
		
		v3[8] = p;
		
		return v3;
	}
	
	public static double mediana(Pixel[] pixels, int canal) {
		double v[] = new double[pixels.length];
		for (int i = 0; i < pixels.length; i++) {
			if(canal == 1) {
				v[i] = pixels[i].r;
			}
			if(canal == 2) {
				v[i] = pixels[i].g;
			}
			if(canal == 3) {
				v[i] = pixels[i].b;
			}
		}
		v = ordenaVetor(v);
		return v[v.length/2];
	}

	private static double[] ordenaVetor(double[] v) {
		double aux;
		
		for (int x = 0; x < v.length; x++) {
			for (int y = x+1; y < v.length; y++) {
			    if(v[x] > v[y] ){
			    	aux = v[x];
			    	v[x] = v[y];
			    	v[y] = aux;
			    }
			}
		}
		
		return v;
	}
	
}
