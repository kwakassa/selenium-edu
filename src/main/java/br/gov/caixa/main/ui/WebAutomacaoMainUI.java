package br.gov.caixa.main.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebAutomacaoMainUI extends JFrame{
	

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(WebAutomacaoMainUI.class);
	private static Integer width = 450;
	private static Integer height = 120;
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static Dimension windowSize = new Dimension(width, height);// Anterior 550,340
	private static final String URL_SIMTX_WEB_DES = "https://10.116.82.126:8020"; 
	private WebDriver driver;
	private WebDriverWait wait;
	
	public WebAutomacaoMainUI() {
		firefoxKillProcess();
		initComponentes();
		initEvents();
	}

	private void initSelenium() {
		FirefoxProfile profile = getFireFoxProfile();
		profile.setAcceptUntrustedCertificates(true);
		profile.setAssumeUntrustedCertificateIssuer(false);
		FirefoxBinary ffBinary = new FirefoxBinary(new File("C:\\c096489\\pgrms\\FirefoxPortable51\\FirefoxPortable.exe"));
		driver = new FirefoxDriver(ffBinary,profile);
		wait = new WebDriverWait(driver, 5);
	}

	private void initComponentes() {
		System.setProperty("webdriver.gecko.driver", "C:\\c096489\\pgrms\\geckodriver-v0.23.0-win64\\geckodriver.exe");
		setTitle("Simtx web Automacao UI");
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(207, Short.MAX_VALUE))
		);
		
		JLabel lblTesteDeLogin = new JLabel("Teste de Login do SIMTX:");
		
		JButton btnTesteLogin = new JButton("Iniciar");
		btnTesteLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void, Void> tarefa = new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {
						initSelenium();
						driver.get(URL_SIMTX_WEB_DES);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
						WebElement txtUsuario = driver.findElement(By.id("name"));
						wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sname")));
						WebElement txtPassword = driver.findElement(By.id("sname"));
						txtUsuario.sendKeys("c988068");
						txtPassword.sendKeys("c988068");//btnLogin
						WebElement btnLogin = driver.findElement(By.xpath("//button[contains(text(),'Entrar')]"));
						btnLogin.click();
						//wait.wait(1000);
						driver.switchTo().defaultContent();
						wait.until(ExpectedConditions.presenceOfElementLocated(By.id("homeAlerts")));
						String pageSource = driver.getPageSource();
						boolean contains = pageSource.contains("Bem vindo ao SIMTX");
						if(contains){
							JOptionPane.showMessageDialog(null, "Teste de Login com Sucesso!");
						}else{
							JOptionPane.showMessageDialog(null, "Falha no Teste de Login!");
						}
						firefoxKillProcess();
						return null;
					}
				};
				tarefa.execute();
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTesteDeLogin)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnTesteLogin)
					.addContainerGap(183, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTesteDeLogin)
						.addComponent(btnTesteLogin))
					.addContainerGap(37, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
		this.setBounds((screenSize.width - windowSize.width) / 2,	(screenSize.height - windowSize.height) / 2, windowSize.width, windowSize.height);
	}
	
	private void initEvents(){
		
	}
	
	private FirefoxProfile getFireFoxProfile(){
		FirefoxProfile firefoxProfile = new FirefoxProfile();
		firefoxProfile.setPreference("browser.download.useDownloadDir", true);
		firefoxProfile.setPreference("browser.download.folderList", 2);
		firefoxProfile.setPreference("browser.download.dir",new JFileChooser().getFileSystemView().getDefaultDirectory()+"");
		firefoxProfile.setPreference("browser.download.manager.showWhenStarting",false);
		firefoxProfile.setPreference("browser.download.manager.useWindow",false);
		firefoxProfile.setPreference("browser.helperApps.alwaysAsk.force", false);
		firefoxProfile.setPreference("browser.download.manager.showAlertOnComplete",false);
		firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk","text/csv"); 
		System.out.println("Firefox Profile Vers\u00e3o: 2.23" );
		return firefoxProfile;
		
	}
	
	private void firefoxKillProcess(){
		try {
			Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");
		} catch (IOException e) {
			logger.error("Erro ao finalizar processo do Firefox!");
		}
	}
	
	public static void main(String[] args) {
		JFrame main = new WebAutomacaoMainUI();
		main.setVisible(true);
	}
}
