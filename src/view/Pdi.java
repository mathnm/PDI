package view;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
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
	
	public static Image adicao(Image img1, Image img2, double ti1, double ti2) {
		int w1 = (int)img1.getWidth();
		int h1 = (int)img1.getHeight();
		int w2 = (int)img2.getWidth();
		int h2 = (int)img2.getHeight();
		int w = Math.min(w1, w2);
		int h = Math.min(h1, h2);
		PixelReader pr1 = img1.getPixelReader();
		PixelReader pr2 = img2.getPixelReader();
		WritableImage wi = new WritableImage(w, h);
		PixelWriter pw = wi.getPixelWriter();
		for (int i = 1; i < w; i++) {
			for (int j = 1; j < h; j++) {
				Color corImg1 = pr1.getColor(i, j);
				Color corImg2 = pr2.getColor(i, j);
				double r = (corImg1.getRed()*ti1)+(corImg2.getRed()+ti2);
				double g = (corImg1.getGreen()*ti1)+(corImg2.getGreen()+ti2);
				double b = (corImg1.getBlue()*ti1)+(corImg2.getBlue()+ti2);
				r = r > 1 ? 1 : r;
				g = g > 1 ? 1 : g;
				b = b > 1 ? 1 : b;
				
				Color nCor = new Color (r,g,b,1);
				pw.setColor(i, j, nCor);
			}
			
		}
		
		return wi;
		
	}
	
	public static Image subtracao(Image img1, Image img2) {
		int w1 = (int)img1.getWidth();
		int h1 = (int)img1.getHeight();
		int w2 = (int)img2.getWidth();
		int h2 = (int)img2.getHeight();
		int w = Math.min(w1, w2);
		int h = Math.min(h1, h2);
		PixelReader pr1 = img1.getPixelReader();
		PixelReader pr2 = img2.getPixelReader();
		WritableImage wi = new WritableImage(w, h);
		PixelWriter pw = wi.getPixelWriter();
		for (int i = 1; i < w; i++) {
			for (int j = 1; j < h; j++) {
				Color vCor1 = pr1.getColor(i, j);
				Color vCor2 = pr2.getColor(i, j);
				double r = (vCor1.getRed())-(vCor2.getRed());
				double g = (vCor1.getGreen())-(vCor2.getGreen());
				double b = (vCor1.getBlue())-(vCor2.getBlue());
				r = r < 0 ? 0 : r;
				g = g < 0 ? 0 : g;
				b = b < 0 ? 0 : b;
				
				Color nCor = new Color (r,g,b,vCor1.getOpacity());
				pw.setColor(i, j, nCor);
			}
			
		}
		
		return wi;
		
	}
	
	public static Image marcacao(Image img, Pixel inicio, Pixel fim) {
		try {
			int w = (int)img.getWidth();
			int h = (int)img.getHeight();
			
			PixelReader pr = img.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();

			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					Color cor = pr.getColor(i, j);
					Color marcacao = Color.CHARTREUSE;
					pw.setColor(i, j, cor);
					
					int aux;
					//VERIFICA DIREÇÃO E SENTIDO DO ARRASTO
					if(inicio.y > fim.y) {
						aux = inicio.y;
						inicio.y = fim.y;
						fim.y = aux;
					}
					
					if(inicio.x > fim.x) {
						aux = inicio.x;
						inicio.x = fim.x;
						fim.x = aux;
					}
										
					
					//COLUNA ESQUERDA
					if (inicio.x == i && j >= inicio.y && j <= fim.y) {
						pw.setColor(i, j, marcacao);
					}
					
					//LINHA SUPERIOR
					if (inicio.y == j && inicio.x <= i && fim.x >= i) {
						pw.setColor(i, j, marcacao);
					}
					
					//COLUNA DIREITA
					if (fim.x == i && fim.y >= j && inicio.y <= j) {
						pw.setColor(i, j, marcacao);
					}
					
					//LINHA INFERIOR
					if (fim.y == j && fim.x >= i && inicio.x <= i) {
						pw.setColor(i, j, marcacao);
					}
				}
			}
			
			return wi;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings({"rawtypes","unchecked"})
	public static void montaGrafico(Image img,BarChart<String, Number> grafico) {
		int[] hist = histogramaUnico(img);
		XYChart.Series vlr = new XYChart.Series();
		
		for (int i = 0; i < hist.length; i++) {
			vlr.getData().add(new XYChart.Data(i+"", hist[i]));
		}
		
		grafico.getData().addAll(vlr);
	}
	
	public static int[] histogramaUnico(Image img) {
		int[] qt = new int[256];
		PixelReader pr = img.getPixelReader();
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				qt[(int)(pr.getColor(i, j).getRed()*255)]++;
				qt[(int)(pr.getColor(i, j).getGreen()*255)]++;
				qt[(int)(pr.getColor(i, j).getBlue()*255)]++;
			}
		}
		return qt;
	}
	
}
