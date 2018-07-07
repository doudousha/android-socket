package wq.com.tcpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.sql.Time;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class TcpActivity extends AppCompatActivity {

    @BindView(R.id.btn_scan)
    public Button btn_scan;

    @BindView(R.id.btn_send)
    public Button btn_send;

    @BindView(R.id.text_status)
    public TextView txt_status;

    @BindView(R.id.edit_msg)
    public EditText edit_msg;

    @BindView(R.id.edit_ipaddress)
    public EditText edit_ipAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        ButterKnife.bind(this);
        Timber.tag("gogogo");

        socket = new Socket();
    }

    private final int port = 9897;

    private int connectionTimeout = 8000;


    @OnClick(R.id.btn_scan)
    public void btn_scan_click(Button btn) {

        Toast.makeText(this, "你好!", Toast.LENGTH_LONG).show();

        Timber.d("btn_scan_click");
        new Thread(() -> {
            for (int i = 2; i < 255; i++) {


                String host = "192.168.0." + i;
                try {
                    Timber.d("try connection:" + host);
                    InetSocketAddress address = new InetSocketAddress(host, port);
                    Socket socket = new Socket();
                    socket.connect(address,1000);

                    runOnUiThread(() -> {
                        edit_ipAddress.setText(host);
                    });

                    Timber.d("try connection success:" + host);
                    socket.close();
                    return ;
                } catch (IOException e) {
                    e.printStackTrace();
                    Timber.d("try connection fail:" + host);
                }
            }
        }).start();
    }

    private Socket socket;


    @OnClick(R.id.btn_send)
    public void btn_send_click(Button btn) {
        String msg = edit_msg.getText().toString();

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        new Thread(() -> {
            try {

                if (socket == null) {
                    socket = new Socket();
                }

                // 2、设置读流的超时时间
                socket.setSoTimeout(8000);

                if(socket.isConnected()==false) {
                    InetSocketAddress address = new InetSocketAddress(edit_ipAddress.getText().toString(), port);
                    socket.connect(address);
                }

                // 3、获取输出流与输入流
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();

                // 4、发送信息
                byte[] sendData = msg.getBytes(Charset.forName("UTF-8"));
                outputStream.write(sendData, 0, sendData.length);
                outputStream.flush();

                // 5、接收信息
                byte[] buf = new byte[1024];
                int len = inputStream.read(buf);
                String receData = new String(buf, 0, len, Charset.forName("UTF-8"));

                Timber.d("收到回复: " + receData);
                Timber.d(receData);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


}