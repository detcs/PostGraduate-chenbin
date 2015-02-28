package com.data.model;

import java.io.IOException;

import com.app.ydd.R;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;

public class MusicService extends Service {

	private MediaPlayer mediaPlayer;
	private Intent intent = null;
	private Bundle bundle = null;
	private String audioPath = null;
	private String receiver = null;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
    public void onCreate() {
        super.onCreate();
       // mediaPlayer=MediaPlayer.create(this,R.raw.song);
 
    }
    @Override  
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        audioPath = intent.getExtras().getString("audioPath");
        receiver = intent.getExtras().getString("bc_receiver");
        //１.正在播放
        //使其暂停播放，并通知界面将Button的值改为"播放"(如果正在播放，Button值是"暂停")
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            sendUpdateUI(1);//更新界面
        }
        //２.正在暂停
        else{
            if(mediaPlayer == null){
                mediaPlayer = new MediaPlayer();//如果被停止了，则为null
                try {
                    mediaPlayer.setDataSource(audioPath);//设置播放的文件的路径
                    mediaPlayer.prepare();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            mediaPlayer.start();
            sendUpdateUI(0);//更新界面
            
        } 
    } 
    @Override
    public void onDestroy() {
        if(mediaPlayer !=null){
            mediaPlayer.release();//停止时要release
            sendUpdateUI(2);//更新界面
        }
        super.onDestroy();
    }
    //send broadcast
    private void sendUpdateUI(int flag) 
    {
        intent = new Intent(receiver);//bc_receiver前面已有定义，是从Activity传过来的
        //如果缺少下面这句，关掉再重新打开播放器里点“停止”并不能停掉
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle = new Bundle();
        bundle.putInt("backFlag", flag);//把flag传回去
        intent.putExtras(bundle);
        sendBroadcast(intent);//发送广播　  //发送后，在Activity里的updateUIReceiver的onReceiver()方法里就能做相应的更新界面的工作了
    }

}
