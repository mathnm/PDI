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
			
//			Tentativa de deixar a área selecionada de cabeça para baixo
//			for (int i = inicio.x; i < fim.x; i++) {
//				for (int j = inicio.y; j < fim.y; j++) {
//					Color corA = pr.getColor(i, j);
//					if(i < inicio.x || i > inicio.x) {
//						pw.setColor(i, j, corN);
//					}else	if(j < inicio.y || j > fim.y) {
//						pw.setColor(i, j, corN);
//					}
//				}
//			}
			
			
			
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
	
	public static Image equalizacaoHistograma(Image img, boolean todos) {
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		PixelReader pr = img.getPixelReader();
		WritableImage wi = new WritableImage(w, h);
		PixelWriter pw = wi.getPixelWriter();
		
		int[] hR = histogramaCanal(img, "R");
		int[] hG = histogramaCanal(img, "G");
		int[] hB = histogramaCanal(img, "B");
		
		int[] histAcR = histogramaAc(hR);
		int[] histAcG = histogramaAc(hG);
		int[] histAcB = histogramaAc(hB);
		
		int qtTonsR = qtTons(hR);
		int qtTonsG = qtTons(hG);
		int qtTonsB = qtTons(hB);
		
		double minR = pontoMin(hR);
		double minG = pontoMin(hG);
		double minB = pontoMin(hB);
		
		if(todos) {
			qtTonsR = 255;
			qtTonsG = 255;
			qtTonsB = 255;
			minR = 0;
			minG = 0;
			minB = 0;
		}
		
		double n = w*h;
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color corAntiga = pr.getColor(i, j);
				
				double acR = histAcR[(int)(corAntiga.getRed()*255)];
				double acG = histAcG[(int)(corAntiga.getGreen()*255)];
				double acB = histAcB[(int)(corAntiga.getBlue()*255)];
				
				double pxR = ((qtTonsR-1)/n)*acR;
				double pxG = ((qtTonsG-1)/n)*acG;
				double pxB = ((qtTonsB-1)/n)*acB;
				
				double corR = (minR+pxR)/255;
				double corG = (minG+pxG)/255;
				double corB = (minB+pxB)/255;
				
				Color corNova = new Color(corR, corG, corB, corAntiga.getOpacity());
				pw.setColor(i, j, corNova);
			}
		}
		return wi;
	}
	
	public static int[] histogramaCanal(Image img, String canal) {
		int[] qt = new int[256];
		
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		PixelReader pr = img.getPixelReader();
		
		if(canal == "R") {
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					qt[(int)(pr.getColor(i, j).getRed()*255)]++;
				}
			}
		}else if (canal == "G") {
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					qt[(int)(pr.getColor(i, j).getGreen()*255)]++;
				}
			}
		}else if (canal == "B") {
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					qt[(int)(pr.getColor(i, j).getBlue()*255)]++;
				}
			}
		}
		return qt;
		
	}
	
	private static int[] histogramaAc(int[] histograma) {
		int total = 0;
		int[] histogramaAc = new int[histograma.length];		
		for (int i = 0; i < histogramaAc.length; i++) {			
			histogramaAc[i] = total + histograma[i];
			total = histogramaAc[i];
		}
		return histogramaAc;
	}
	
	private static int qtTons(int[] histograma) {
		int qt = 255;
		for (int i = 0; i < histograma.length; i++) {
			if (histograma[i] == 0) {
				qt--;				
			}
		}
		return qt;
   	}

	private static int pontoMin(int[] histograma) {
		for (int i = 0; i < histograma.length; i++) {
			if (histograma[i]>0) {
				return i;
			}
		}
		return 0;
	}
	
	//-------------------------------------------EXERCÍCIOS PROVAS ANTIGAS-------------------------------------------
	
	//Zebrado: 
	public static Image zebrado (Image img, int nrColunas) {
		
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		
		PixelReader pr = img.getPixelReader();
		WritableImage wi = new WritableImage(w, h);
		PixelWriter pw = wi.getPixelWriter();
		
		int widthColuna = (int)(w/nrColunas);
		int colunaSomador = widthColuna;
		double media;
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color corA = pr.getColor(i,j);
				media = (corA.getRed()+corA.getGreen()+corA.getBlue())/3;
				Color corN = new Color(media, media, media, corA.getOpacity());
				if(i<widthColuna) {
					pw.setColor(i, j, corN);
				}else {
					pw.setColor(i, j, corA);
				}
			}
			if(i == (widthColuna*2)) {
				widthColuna += colunaSomador+colunaSomador;
			}
		}
		return wi;
		
	}
	
	public static String verificaQuadrado (Image img) {
		
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		
		PixelReader pr = img.getPixelReader();
		
		Color preto = Color.BLACK;
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if(pr.getColor(i, j).equals(preto)) {
					if(pr.getColor(i+1, j+1).equals(preto)) {
						return "PREENCHIDO";
					}else {
						return "VAZIO";
					}
				}
			}
		}
		return null;
	}
	
	public static Image insereMoldura (Image img, Color cor, int largura) {
		
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		
		PixelReader pr = img.getPixelReader();
		WritableImage wi = new WritableImage(w, h);
		PixelWriter pw = wi.getPixelWriter();
		
		
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
		
				Color corOriginal = pr.getColor(i, j);
				
				if(i < largura) {
					pw.setColor(i, j, cor);
				}else 
				
				if(j < largura) {
					pw.setColor(i, j, cor);
				}else 
				
				if(i>=(w-largura)) {
					pw.setColor(i, j, cor);
				}else 
				
				if(j>=(h-largura)) {
					pw.setColor(i, j, cor);
				}else {
					pw.setColor(i, j, corOriginal);
				}
				
			}
		}
		
		return wi;
		
	}
	
	public static Image filtroDuplo (Image img) {
		
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		
		PixelReader pr = img.getPixelReader();
		WritableImage wi = new WritableImage(w, h);
		PixelWriter pw = wi.getPixelWriter();
		
		int meio = (int)(h/2);
		
		double media;
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color corA = pr.getColor(i, j);
				media = (corA.getRed()+corA.getGreen()+corA.getBlue())/3;
				if(j<meio) {
					Color negativa = new Color (1 - corA.getRed(),
											1 - corA.getGreen(),
											1 - corA.getBlue(),
											corA.getOpacity());
					pw.setColor(i, j, negativa);
				}else {
					Color cinza = new Color(media, media, media, corA.getOpacity());
					pw.setColor(i, j, cinza);
				}
			}
		}
		
		return wi;
		
	}
	
	public static String identificaForma(Image img) {
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		
		PixelReader pr = img.getPixelReader();
		
		Color preto = Color.BLACK;
		
		String resposta;
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if(pr.getColor(i, j).equals(preto)) {
					if(pr.getColor(i, j+1).equals(preto) &&  pr.getColor(i+1, j).equals(preto)) {
						resposta = "q";
						return resposta;
					}else {
						resposta = "c";
						return resposta;
					}
				}
			}
		}
		return null;
	}
	
	public static Image insereJaula(Image img, int distancia, Color cor) {
		
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		
		PixelReader pr = img.getPixelReader();
		WritableImage wi = new WritableImage(w, h);
		PixelWriter pw = wi.getPixelWriter();
		
		int distanciaOriginal = distancia;
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color corOriginal = pr.getColor(i, j);
				if(i>=distancia && i <= (distancia+3)) {
					pw.setColor(i, j, cor);
					if(i == distancia+3) {
						distancia = distancia + distanciaOriginal;
					}
				}else {
					pw.setColor(i, j, corOriginal);
				}
			}
		}
		
		return wi;
		
	}
	
	public static String identificaCores(Image img, int x1, int x2, int y1, int y2) {
			
		PixelReader pr = img.getPixelReader();
		
		String resposta = "";
		
		boolean temVermelho = false;
		boolean temAzul = false;
		boolean temVerde = false;
		
		for (int i = x1; i < x2; i++) {
			for (int j = y1; j < y2; j++) {
				
				if(pr.getColor(i, j).equals(Color.RED)) {
					temVermelho = true;
				}
				if(pr.getColor(i, j).equals(Color.GREEN)) {
					temVerde = true;
				}
				if(pr.getColor(i, j).equals(Color.BLUE)) {
					temAzul = true;
				}
			}
		}
		
		if(temAzul) {
			resposta += "-azul-";
		}
		if(temVermelho) {
			resposta += "-vermelho-";
		}
		if(temVerde) {
			resposta += "-verde-";
		}
		
		return resposta;
	}
	
	//PROVA
	
	public static String verificaRetangulo(Image img) {
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		
		PixelReader pr = img.getPixelReader();
		
		boolean primeiroPixelPreto = false;
		int x1 = 0;
		int y1 = 0;
		int x2 = 0;
		int y2 = 0;
		String resposta = "fechado";
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if(pr.getColor(i, j).equals(Color.BLACK) && !primeiroPixelPreto) {
					x1 = i;
					y1 = j;
					primeiroPixelPreto = true;
				}
				if(j-1 >= 0 && i-1 >= 0 && i+1 < w && j+1 < h) {
					if(pr.getColor(i, j+1).equals(Color.WHITE) && pr.getColor(i, j-1).equals(Color.BLACK) && pr.getColor(i+1, j).equals(Color.WHITE) && pr.getColor(i-1, j).equals(Color.BLACK)) {
						x2 = i;
						y2 = j;
					}
				}
			}
		}
		
		for (int i = x1; i <= x2; i++) {
			for (int j = y1; j <= y2; j++) {
				if(pr.getColor(x1, j).equals(Color.WHITE)) {
					resposta = "aberto";
				}
				if(pr.getColor(x2, j).equals(Color.WHITE)) {
					resposta = "aberto";
				}
			}
			if(pr.getColor(i, y1).equals(Color.WHITE)) {
				resposta = "aberto";
			}
			if(pr.getColor(i, y2).equals(Color.WHITE)) {
				resposta = "aberto";
			}
		}
		
		return resposta;
		
	}
	
	public static Image equalizaDiagonalPrincipal(Image img) {
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		PixelReader pr = img.getPixelReader();
		WritableImage wi = new WritableImage(w, h);
		PixelWriter pw = wi.getPixelWriter();
		
		
		
		int[] hR = histogramaCanal(img, "R");
		int[] hG = histogramaCanal(img, "G");
		int[] hB = histogramaCanal(img, "B");
		
		int[] histAcR = histogramaAc(hR);
		int[] histAcG = histogramaAc(hG);
		int[] histAcB = histogramaAc(hB);
		
		int qtTonsR = 255;
		int qtTonsG = 255;
		int qtTonsB = 255;
		
		double minR = 0;
		double minG = 0;
		double minB = 0;
			
		double n = w*h;
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if(i==j) {
					pw.setColor(i, j, Color.BLACK);
				}else if(i>j){
					Color corAntiga = pr.getColor(i, j);
				
					double acR = histAcR[(int)(corAntiga.getRed()*255)];
					double acG = histAcG[(int)(corAntiga.getGreen()*255)];
					double acB = histAcB[(int)(corAntiga.getBlue()*255)];
					
					double pxR = ((qtTonsR-1)/n)*acR;
					double pxG = ((qtTonsG-1)/n)*acG;
					double pxB = ((qtTonsB-1)/n)*acB;
					
					double corR = (minR+pxR)/255;
					double corG = (minG+pxG)/255;
					double corB = (minB+pxB)/255;
					
					Color corNova = new Color(corR, corG, corB, corAntiga.getOpacity());
					pw.setColor(i, j, corNova);
				}else {
					pw.setColor(i, j, pr.getColor(i, j));
				}
			}
		}
		return wi;
	}
	
	public static Image inverteQuadrante(Image img, boolean q1, boolean q2, boolean q3, boolean q4) {
		
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		PixelReader pr = img.getPixelReader();
		WritableImage wi = new WritableImage(w, h);
		PixelWriter pw = wi.getPixelWriter();
		
		if(q1) {
			int width = (int)(w/2);
			int height = (int)(h/2);
			
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					pw.setColor(i, j, pegaCorInversa(img, i, j));
				}
			}			
		}else {
			int width = (int)(w/2);
			int height = (int)(h/2);
			
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					pw.setColor(i, j, pr.getColor(i, j));
				}
			}	
		}
		
		if(q2) {
			int width = (int)(w/2);
			int height = (int)(h/2);
			
			for (int i = width; i < w; i++) {
				for (int j = 0; j < height; j++) {
					pw.setColor(i, j, pegaCorInversa(img, i, j));
				}
			}			
		}else {
			int width = (int)(w/2);
			int height = (int)(h/2);
			
			for (int i = width; i < w; i++) {
				for (int j = 0; j < height; j++) {
					pw.setColor(i, j, pr.getColor(i, j));
				}
			}
		}
		
		if(q3) {
			int width = (int)(w/2);
			int height = (int)(h/2);
			
			for (int i = 0; i < width; i++) {
				for (int j = height; j < h; j++) {
					pw.setColor(i, j, pegaCorInversa(img, i, j));
				}
			}			
		}else {
			int width = (int)(w/2);
			int height = (int)(h/2);
			
			for (int i = 0; i < width; i++) {
				for (int j = height; j < h; j++) {
					pw.setColor(i, j, pr.getColor(i, j));
				}
			}	
		}
		
		if(q4) {
			int width = (int)(w/2);
			int height = (int)(h/2);
			
			for (int i = width; i < w; i++) {
				for (int j = height; j < h; j++) {
					pw.setColor(i, j, pegaCorInversa(img, i, j));
				}
			}			
		}else {
			int width = (int)(w/2);
			int height = (int)(h/2);
			
			for (int i = width; i < w; i++) {
				for (int j = height; j < h; j++) {
					pw.setColor(i, j, pr.getColor(i, j));
				}
			}	
		}
		
//		for (int i = 0; i < w; i++) {
//			for (int j = 0; j < h; j++) {
//				pw.setColor(i, j, pegaCorInversa(img, i, j));
//			}
//		}
		
		return wi;
		
	}
	
	public static Color pegaCorInversa(Image img, int width, int height) {
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		PixelReader pr = img.getPixelReader();
		
		return pr.getColor(w - 1 - width, h - 1 - height);
	}
	
}
