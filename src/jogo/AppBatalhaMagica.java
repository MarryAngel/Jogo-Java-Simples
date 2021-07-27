package jogo;
//Movimentação com wasd e ataque na barra de espaço
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//import java.awt.event.*;
//import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

class AppBatalhaMagica extends JFrame {
	private static final long serialVersionUID = 1L;
	Image imgJogo[] = new Image[2];
	Image imgFogo[] = new Image[20];
	Image imgGelo[] = new Image[20];
	Desenho des = new Desenho();
	Timer t;
	int xFogo=10, xGelo=1000, yFogo=400, yGelo=400, velyFogo=0;
	int estadoFogo=0, estadoGelo=0, vidaFogo=400, vidaGelo=400;
	int dirFogo=1, dirGelo=-1;
	int viradoFogo=0;
	int animacaoFogo, gravidade=10, dano=100, animacaoGelo=0;
	boolean saltoFogo=false, abaixadoFogo=false, mortoFogo=false, ataqueFogo=false, saltoGelo=false, abaixadoGelo=false, morteGelo=false;
	
	public int prendedor(int num, int min, int max) {
		if(num<min)
			return min;
		if(num>max)
			return max;
		return num;
	}
	
	public void reset(){
		vidaGelo=400;
		vidaFogo=400;
		xGelo=1000;
		xFogo=10;
		dirFogo=1;
		dirGelo=-1;
		morteGelo=false;
	}
	
	class Desenho extends JPanel {
		private static final long serialVersionUID = 1L;
		String caminho="src/jogo/";
		
		Desenho() {
	      try {
	        setPreferredSize(new Dimension(1095, 616));
	        imgJogo[0] = ImageIO.read(new File(""+caminho+"fundoFloresta.png"));
	        //Imagens referentes ao mago de fogo
	        imgFogo[0] = ImageIO.read(new File(""+caminho+"FogoParado.png"));
	        imgFogo[1] = ImageIO.read(new File(""+caminho+"FogoAnda0.png"));
	        imgFogo[2] = ImageIO.read(new File(""+caminho+"FogoAnda1.png"));
	        imgFogo[3] = ImageIO.read(new File(""+caminho+"FogoAnda2.png"));
	        imgFogo[4] = ImageIO.read(new File(""+caminho+"FogoAnda3.png"));
	        imgFogo[5] = ImageIO.read(new File(""+caminho+"FogoAnda4.png"));
	        imgFogo[6] = ImageIO.read(new File(""+caminho+"FogoPula0.png"));
	        imgFogo[7] = ImageIO.read(new File(""+caminho+"FogoPula1.png"));
	        imgFogo[8] = ImageIO.read(new File(""+caminho+"FogoPula2.png"));
	        imgFogo[9] = ImageIO.read(new File(""+caminho+"FogoPula3.png"));
	        imgFogo[10] = ImageIO.read(new File(""+caminho+"FogoPula4.png"));
	        imgFogo[11] = ImageIO.read(new File(""+caminho+"FogoMorto1.png"));
	        imgFogo[12] = ImageIO.read(new File(""+caminho+"FogoMorto2.png"));
	        imgFogo[13] = ImageIO.read(new File(""+caminho+"FogoAtaque1.png"));
	        imgFogo[14] = ImageIO.read(new File(""+caminho+"FogoAtaque2.png"));
	        imgFogo[15] = ImageIO.read(new File(""+caminho+"FogoAtaque3.png"));
	        imgFogo[16] = ImageIO.read(new File(""+caminho+"FogoAtaque4.png"));
	        //Imagens referente ao mago de gelo
	        imgGelo[0] = ImageIO.read(new File(""+caminho+"GeloParado.png"));
	        imgGelo[1] = ImageIO.read(new File(""+caminho+"GeloMorto1.png"));
	        imgGelo[2] = ImageIO.read(new File(""+caminho+"GeloMorto2.png"));
	        imgGelo[3] = ImageIO.read(new File(""+caminho+"GeloMorto3.png"));
	        imgGelo[4] = ImageIO.read(new File(""+caminho+"GeloMorto4.png"));
	      } catch (IOException e) {
	        JOptionPane.showMessageDialog(this, "A imagem não pode ser carregada!\n" + e, "Erro", JOptionPane.ERROR_MESSAGE);
	        System.exit(1);
	      }
	    }
	
	    public void paintComponent(Graphics g) {
	      super.paintComponent(g);
	      g.drawImage(imgJogo[0], 0, 0, getSize().width, getSize().height, this);
	      g.drawImage(imgFogo[estadoFogo], xFogo, yFogo, dirFogo*imgFogo[estadoFogo].getWidth(this)*1/2, imgFogo[estadoFogo].getHeight(this)*1/2, this);
	      g.drawImage(imgGelo[estadoGelo], xGelo, yGelo, dirGelo*imgGelo[estadoGelo].getWidth(this)*1/2, imgGelo[estadoGelo].getHeight(this)*1/2, this);
	      //Desenhos para a barra de vida
	      g.setColor(new Color(110,110,110)); 
	      g.fillRect(10, 20, 400, 50);
	      g.fillRect(getSize().width-410, 20, 400, 50);
	      g.setColor(new Color(255,130*vidaFogo/400,0)); //255,0,0
	      g.fillRect(10, 20, vidaFogo, 50);
	      g.setColor(new Color(255-255*vidaGelo/400,200*vidaGelo/400,255*vidaGelo/400));
	      g.fillRect(getSize().width-10, 20, -vidaGelo, 50);
	      //System.out.println("YFogo" + yFogo + ", Dir: "+dirFogo);
	      Toolkit.getDefaultToolkit().sync();
	    }
	}

	  void tick() {
		  if(morteGelo) {
			  animacaoGelo++;
			  if(animacaoGelo>8) {
				  estadoGelo=0;
				  JOptionPane.showMessageDialog(this, "Gelo perdeu!");
				  reset();
			  }
			  else
			   estadoGelo=animacaoGelo/2;
			  
		  }
		  yFogo=prendedor(yFogo+velyFogo,0,400);
		  velyFogo=prendedor(velyFogo+gravidade,-50,50);
		  vidaFogo=prendedor(vidaFogo,0,400);
		  vidaGelo=prendedor(vidaGelo,0,400);
		  if(saltoFogo) {
			  animacaoFogo++;
			  if(animacaoFogo>10) {
				  estadoFogo=0;
				  saltoFogo=false;
			  }
			  else
			   estadoFogo=animacaoFogo/2+5;
			  System.out.println("animacaoFogo = " + animacaoFogo);
		  }
		  else if(abaixadoFogo) {
			  animacaoFogo++;
			  if(animacaoFogo>2) {
				  estadoFogo=0;
				  abaixadoFogo=false;
			  }
			  else
				  estadoFogo=animacaoFogo+10;
		  }
		  else if(ataqueFogo) {
			  animacaoFogo++;
			  if(animacaoFogo>6) {
				  estadoFogo=0;
				  ataqueFogo=false;
  				if(Math.abs(xFogo-xGelo)<500 && Math.abs(xFogo-xGelo)>100) {
  				  if((xFogo-xGelo>0 && dirFogo<0) || (xFogo-xGelo<0 && dirFogo>0)) {
  					if(!(abaixadoGelo || saltoGelo))
  						vidaGelo=prendedor(vidaGelo-dano,0,400);
  					if(vidaGelo<=0) {
  						animacaoGelo=0;
  						morteGelo=true;
  					}
  				  }
				}
			  }
			  else
				  estadoFogo=animacaoFogo/2+13;
		  }
		  des.repaint();
	 }
	
	AppBatalhaMagica() {
		super("Batalha Magica");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(des);
		pack();
		setVisible(true);
	    addKeyListener(new KeyAdapter() {
	    	public void keyPressed(KeyEvent e) {
	    		if(e.getKeyCode() == KeyEvent.VK_D) {
	    			if(dirFogo==-1) {
	    				xFogo-=imgFogo[estadoFogo].getWidth(des)*1/2;
	    				dirFogo=1;
	    			}
	    			xFogo=prendedor(xFogo+10, -10, 900);
	    			estadoFogo++;
	    			if(estadoFogo==6)
	    				estadoFogo=0;
	    			repaint();
	    		}
	    		else if(e.getKeyCode() == KeyEvent.VK_A) {
	    			if(dirFogo==1) {
	    				xFogo+=imgFogo[estadoFogo].getWidth(des)*1/2;
	    			}
	    			dirFogo=-1;
	    			xFogo=prendedor(xFogo-10, -10+imgFogo[estadoFogo].getWidth(des)*1/2, 900+imgFogo[estadoFogo].getWidth(des)*1/2);
	    			estadoFogo++;
	    			if(estadoFogo==6)
	    				estadoFogo=0;	
	    			repaint();
	    		}
	    		else if(e.getKeyCode() == KeyEvent.VK_W) {
	    				if(!saltoFogo) {
	    					velyFogo=-50;
		    				saltoFogo=true;
		    				animacaoFogo=0;
	    				}
	    		   }
	    		
	    		else if(e.getKeyCode() == KeyEvent.VK_S) {
	    			if(!abaixadoFogo) {
	    				abaixadoFogo=true;
	    				animacaoFogo=0;
	    			}
	    		}
	    		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
	    			if(!ataqueFogo) {
	    				ataqueFogo=true;
	    				animacaoFogo=0;
	    			}
	    		}
	    	}
	    });
	    t = new Timer(100, new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
	    		tick();
	    	}
	    });
	    t.start();
	}
	
	public static void main(String[] args) {
		new AppBatalhaMagica();
	}
}
