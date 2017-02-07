

/**
 * Created by mahendra kumar on 04-02-2017.
 */
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;



public class PC_server {

    private static ServerSocket server = null;
    private static Socket client = null;
    private static BufferedReader in = null;
    private static String line;
    private static boolean isConnected=true;
    private static Robot robot;
    private static final int SERVER_PORT = 8998;

    //******************thread to send image**********************
    private static class ConnectedThread extends Thread {
        private final OutputStream os;
        private final String fileName;
        public ConnectedThread(OutputStream outputs,String fname){
            os=outputs;
            fileName=fname;
			System.out.println("constructor of thread initialised");
        }
		public void run(){
			System.out.println("you r in thread");
            try {
                sendFile();
            } catch (Exception e) {
				System.out.println("exeception is there");
                e.printStackTrace();
            }
        }
        public  void sendFile() throws Exception
        {

             System.out.println("in sendFile function");
            // File to send
            File myFile = new File(fileName);
            int fSize = (int) myFile.length();
            if (fSize < myFile.length())
            {
                System.out.println("File is too big'");
                throw new IOException("File is too big.");
            }
              System.out.println("you have got file size");
            // Send the file's size
            byte[] bSize = new byte[4];
            bSize[0] = (byte) ((fSize & 0xff000000) >> 24);
            bSize[1] = (byte) ((fSize & 0x00ff0000) >> 16);
            bSize[2] = (byte) ((fSize & 0x0000ff00) >> 8);
            bSize[3] = (byte) (fSize & 0x000000ff);
			System.out.println(" file size converted into bytes");
            // 4 bytes containing the file size
            os.write(bSize, 0, 4);
               System.out.println(" file size sent");
            // In case of memory limitations set this to false
            boolean noMemoryLimitation = false;
                   
            FileInputStream fis = new FileInputStream(myFile);
			System.out.println(" fis");
            BufferedInputStream bis = new BufferedInputStream(fis);
			System.out.println(" bis");
			
			System.out.println("different streams made");
            try
            {
                if (noMemoryLimitation)
                {
                    // Use to send the whole file in one chunk
                    byte[] outBuffer = new byte[fSize];
                    int bRead = bis.read(outBuffer, 0, outBuffer.length);
                    os.write(outBuffer, 0, bRead);
					System.out.println("no memory location os.write");
                }
                else
                {
                    // Use to send in a small buffer, several chunks
                    int bRead = 0;
                    byte[] outBuffer = new byte[8 * 1024];
                    while ((bRead = bis.read(outBuffer, 0, outBuffer.length)) > 0)
                    {
                        os.write(outBuffer, 0, bRead);
                    }
					System.out.println("else.........no memory location os.write");
                }
                os.flush();
				System.out.println("os.flush");
            }
            finally
            {
                bis.close();
				System.out.println("bis closed");
            }
        }



    }


    //****************************************



    //*****************************************
    public static void main(String[] args) {
        boolean leftpressed=false;
        boolean rightpressed=false;

        try{
            robot = new Robot();
            server = new ServerSocket(SERVER_PORT); //Create a server socket on port 8998
            client = server.accept(); //Listens for a connection to be made to this socket and accepts it
            in = new BufferedReader(new InputStreamReader(client.getInputStream())); //the input stream where data will come from client
        }catch (IOException e) {
            System.out.println("Error in opening Socket");
            System.exit(-1);
        }catch (AWTException e) {
            System.out.println("Error in creating robot instance");
            System.exit(-1);
        }

        //read input from client while it is connected
        while(isConnected){
            try{
                line = in.readLine(); //read input from client
                System.out.println(line); //print whatever we get from client

                //if user clicks on next
                if(line.equalsIgnoreCase("next")){
                    //Simulate press and release of key 'n'
                    robot.keyPress(KeyEvent.VK_N);
                    robot.keyRelease(KeyEvent.VK_N);
                }
                //if user clicks on previous
                else if(line.equalsIgnoreCase("previous")){
                    //Simulate press and release of key 'p'
                    robot.keyPress(KeyEvent.VK_P);
                    robot.keyRelease(KeyEvent.VK_P);
                }
                //if user clicks on play/pause
                else if(line.equalsIgnoreCase("play")){
                    //Simulate press and release of spacebar
                    robot.keyPress(KeyEvent.VK_SPACE);
                    robot.keyRelease(KeyEvent.VK_SPACE);
                }

                //--changes
                //if user clicks on Escape
                else if(line.equalsIgnoreCase("esc")){
                    //Simulate press and release of spacebar
                    robot.keyPress(KeyEvent.VK_ESCAPE);
                    robot.keyRelease(KeyEvent.VK_ESCAPE);
                }

                //if user clicks on UP
                else if(line.equalsIgnoreCase("up")){
                    //Simulate press and release of spacebar
                    robot.keyPress(KeyEvent.VK_UP);
                    robot.keyRelease(KeyEvent.VK_UP);
                }
                //if user clicks on DOWN
                else if(line.equalsIgnoreCase("down")){
                    //Simulate press and release of spacebar
                    robot.keyPress(KeyEvent.VK_DOWN);
                    robot.keyRelease(KeyEvent.VK_DOWN);
                }
                //if user clicks on LEFT
                else if(line.equalsIgnoreCase("left")){
                    //Simulate press and release of spacebar
                    robot.keyPress(KeyEvent.VK_LEFT);
                    robot.keyRelease(KeyEvent.VK_LEFT);
                }
                //if user clicks on RIGHT
                else if(line.equalsIgnoreCase("right")){
                    //Simulate press and release of spacebar
                    robot.keyPress(KeyEvent.VK_RIGHT);
                    robot.keyRelease(KeyEvent.VK_RIGHT);
                }
                //---------------------------------------------------------------
                //input will come in x,y format if user moves mouse on mousepad
                else if(line.contains(",")){
                    float movex=Float.parseFloat(line.split(",")[0]);//extract movement in x direction
                    float movey=Float.parseFloat(line.split(",")[1]);//extract movement in y direction
                    Point point = MouseInfo.getPointerInfo().getLocation(); //Get current mouse position
                    float nowx=point.x;
                    float nowy=point.y;
                    robot.mouseMove((int)(nowx+movex),(int)(nowy+movey));//Move mouse pointer to new location
                }
                //if user taps on mousepad to simulate a left click
                else if(line.contains("left_click")){
                    //Simulate press and release of mouse button 1(makes sure correct button is pressed based on user's dexterity)
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                }
                else if(line.contains("right_click")){
                    //Simulate press and release of mouse button 1(makes sure correct button is pressed based on user's dexterity)
                    robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                }
                else if(line.equalsIgnoreCase("snap")){
                    robot.keyPress(KeyEvent.VK_WINDOWS);
                    robot.keyPress(KeyEvent.VK_PRINTSCREEN);
                    robot.keyRelease(KeyEvent.VK_WINDOWS);
                    robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
                    //********************
                    try
                    {

                        //client = server.accept();

                        ObjectOutputStream outputStream =
                                new ObjectOutputStream(client.getOutputStream());

                        ConnectedThread connectedThread=new ConnectedThread(outputStream, "C:\\Users\\mahendra kumar\\Desktop\\warren.png");
                        // The file name must be a fully qualified path
                        connectedThread.start();
						System.out.println("file is sending");
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    //*******************
                }

                //Exit if user ends the connection
                else if(line.equalsIgnoreCase("exit")){
                    isConnected=false;
                    //Close server and client socket
                    server.close();
                    client.close();
                }
            } catch (IOException e) {
                System.out.println("Read failed");
                System.exit(-1);
            }
        }
    }
}
