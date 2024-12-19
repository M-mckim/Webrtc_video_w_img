////package com.arashivision.sdk.demo.activity;
//
//import android.graphics.Bitmap;
//import android.graphics.PixelFormat;
//import android.media.Image;
//import android.media.ImageReader;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.util.Log;
//import android.widget.ToggleButton;
//import android.os.AsyncTask;
//
//
//import androidx.annotation.Nullable;
//
//import com.arashivision.sdk.demo.R;
//import com.arashivision.sdkcamera.camera.InstaCameraManager;
//import com.arashivision.sdkcamera.camera.callback.IPreviewStatusListener;
//import com.arashivision.sdkcamera.camera.preview.VideoData;
//import com.arashivision.sdkmedia.player.capture.CaptureParamsBuilder;
//import com.arashivision.sdkmedia.player.capture.InstaCapturePlayerView;
//import com.arashivision.sdkmedia.player.listener.PlayerViewListener;
//
//import org.webrtc.RtpReceiver;
//import org.webrtc.VideoTrack;
//import org.webrtc.DataChannel;
//import org.webrtc.IceCandidate;
//import org.webrtc.MediaConstraints;
//import org.webrtc.MediaStream;
//import org.webrtc.PeerConnection;
//import org.webrtc.PeerConnectionFactory;
//import org.webrtc.SdpObserver;
//import org.webrtc.SessionDescription;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.nio.ByteBuffer;
//import java.util.ArrayList;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//
///**
// * 预览时，使用ImageReader，传参Surface，可获取当前预览画面的Bitmap
// *
// * When previewing, use ImageReader and pass in Surface to get
// * the Bitmap of the current preview stream
// */
//public class WebRTCActivity extends BaseObserveCameraActivity implements IPreviewStatusListener {
//
//    private final static String TAG = WebRTCActivity.class.getName();
//
//    private InstaCapturePlayerView mCapturePlayerView;
//    private ToggleButton mBtnSwitch;
//
//    private ImageReader mImageReader;
//    private HandlerThread mImageReaderHandlerThread;
//    private Handler mImageReaderHandler;
//
//    private byte[] beforeData;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_webrtc);
//        setTitle(R.string.preview3_toolbar_title);
////        bindViews();
////
////        // Auto open preview after page gets focus
////        mCapturePlayerView.post(() -> {
////            InstaCameraManager.getInstance().setPreviewStatusChangedListener(this);
////            InstaCameraManager.getInstance().startPreviewStream();
////        });
//        WebRTCClient WC = new WebRTCClient();
//
//        WC.createPeerConnection();
//
//        WC.createDataChannel();
//
//        WC.peerConnection.createOffer(new SdpObserver() {
//            @Override
//            public void onCreateSuccess(SessionDescription sessionDescription) {
//                // Set local description
//                // Offer or Answer 생성 성공 시 호출됨
//                Log.d(TAG, "onCreateSuccess: " + sessionDescription.type);
//                // 생성된 SDP 설정을 로컬 SDP로 설정하고 상대방에게 전송하는 작업을 수행
//                WC.peerConnection.setLocalDescription(new SdpObserver() {
//                    @Override
//                    public void onCreateSuccess(SessionDescription sessionDescription) {
//                        //peerConnection.setLocalDescription(sdpObserver, sessionDescription);
//                        // Local description set successfully
//                        // Now you can get the local description
//                        Log.d(TAG, "onCreateSuccess-setLocalDescription: " + sessionDescription.type);
//                        SessionDescription localDescription = WC.peerConnection.getLocalDescription();
//
//                        if (localDescription != null) {
//                            // Local description is available
//                            // You can access it using localDescription.description
//                            Log.d(TAG, "Local Description: " + localDescription.description);
//                        } else {
//                            // Local description is not available
//                            Log.d(TAG, "Local Description is null");
//                        }
//                    }
//
//                    // Implement other SdpObserver methods
//                    @Override
//                    public void onSetSuccess() {
//                        // setLocalDescription 또는 setRemoteDescription 성공 시 호출됨
//                        Log.d(TAG, "onSetSuccess-setLocalDescription");
//                        // 상대방에게 전달된 SDP 설정이 완료되었을 때 수행할 작업을 처리
//                    }
//                    @Override
//                    public void onCreateFailure(String s) {
//                        Log.e(TAG, "onCreateFailure-setLocalDescription");
//                    }
//                    @Override
//                    public void onSetFailure(String s) {
//                        Log.e(TAG, "onSetFailure-setLocalDescription");
//                    }
//                }, sessionDescription);
//            }
//
//
//            // Implement other SdpObserver methods
//            @Override
//            public void onSetSuccess() {
//                // setLocalDescription 또는 setRemoteDescription 성공 시 호출됨
//                Log.d(TAG, "onSetSuccess");
//                // 상대방에게 전달된 SDP 설정이 완료되었을 때 수행할 작업을 처리
//            }
//            @Override
//            public void onCreateFailure(String s) {
//                Log.e(TAG, "onCreateFailure");
//            }
//            @Override
//            public void onSetFailure(String s) {
//                Log.e(TAG, "onSetFailure");
//            }
//        }, new MediaConstraints());
//
////        String localSDP = String.valueOf(WC.peerConnection.getLocalDescription().description);
////        Log.i("info","localSDP : "+localSDP);
////        WebRTCClient.SendOfferTask sendOfferTask = WC.new SendOfferTask(localSDP);
////        sendOfferTask.execute();
//
//    }
//    // WebRTC
//    public static class WebRTCClient {
//        private static final String TAG = "WebRTCClient";
//        public WebRTCClient.SendOfferTask SendOfferTask;
//
//        private PeerConnectionFactory peerConnectionFactory;
//        PeerConnection peerConnection;
//        private DataChannel dataChannel;
//
//        private DataChannel dataChannel2;
//
//
//        private DataChannel.Observer dataChannelObserver = new DataChannel.Observer() {
//            @Override
//            public void onBufferedAmountChange(long l) {
//                // 데이터 채널의 버퍼 크기가 변경되었을 때 호출됨
//                Log.d(TAG, "onBufferedAmountChange: " + l);
//            }
//
//            @Override
//            public void onStateChange() {
//                // 데이터 채널의 상태 변경 시 호출됨
//                Log.d(TAG, "onStateChange: " + dataChannel.state());
//                if (dataChannel.state() == DataChannel.State.OPEN){
//                    Log.d(TAG, "onStateChange: " + dataChannel.label());
//                    Log.d(TAG, "onStateChange: " + dataChannel.id());
//                    Log.d(TAG, "onStateChange: " + dataChannel.state());
//                    Log.d(TAG, "onStateChange: " + dataChannel);
//
//                    String message = "Hello, World!";
//                    DataChannel.Buffer buffer = new DataChannel.Buffer(ByteBuffer.wrap(message.getBytes()), false);
//                    dataChannel.send(buffer);
//                }
//            }
//
//            @Override
//            public void onMessage(DataChannel.Buffer buffer) {
//                // 데이터 채널에서 메시지를 수신할 때 호출됨
//                Log.d(TAG, "onMessage: " + buffer.toString());
////                Log.e("ERROR!","onMessage error : ");
//                // 메시지 처리 작업 수행
//                // buffer에서 데이터를 추출하여 사용
//            }
//        };
//
//        public WebRTCClient() {
//            // PeerConnectionFactory 초기화
//            PeerConnectionFactory.initialize(
//                    PeerConnectionFactory.InitializationOptions.builder(getApplicationContext())
//                            .createInitializationOptions()
//            );
//
//            // PeerConnectionFactory 생성
//            PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
//            peerConnectionFactory = PeerConnectionFactory.builder()
//                    .setOptions(options)
//                    .createPeerConnectionFactory();
//            Log.d(TAG, "WebRTCClient initialize and peerConnectionFactory create");
//        }
//
//        public void createPeerConnection() {
//            // PeerConnection 생성
//            ArrayList<PeerConnection.IceServer> iceServers = new ArrayList<>();
//            // IceServer 설정 추가
//            iceServers.add(PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer());
//
//            // PeerConnection 설정
//            PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServers);
//            peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, new PeerConnection.Observer() {
//                @Override
//                public void onIceCandidate(IceCandidate iceCandidate) {
//                    // 로컬 ICE 후보가 생성되었을 때 호출됨
//                    Log.d(TAG, "onIceCandidate: " + iceCandidate.toString());
//                    // 상대방에게 전달할 ICE 후보를 처리
//                }
//
//                @Override
//                public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
//                    // 로컬 ICE 후보가 제거되었을 때 호출됨
//                    Log.d(TAG, "onIceCandidatesRemoved");
//                    // 상대방에게 제거된 ICE 후보를 처리
//                }
//
//                @Override
//                public void onSignalingChange(PeerConnection.SignalingState signalingState) {
//                    // 시그널링 상태 변경 시 호출됨
//                    Log.d(TAG, "onSignalingChange: " + signalingState.toString());
//                }
//
//                @Override
//                public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
//                    // ICE 연결 상태 변경 시 호출됨
//                    Log.d(TAG, "onIceConnectionChange: " + iceConnectionState.toString());
//                }
//
//                @Override
//                public void onIceConnectionReceivingChange(boolean b) {
//                    // ICE 연결 수신 상태 변경 시 호출됨
//                    Log.d(TAG, "onIceConnectionReceivingChange: " + b);
//                }
//
//                @Override
//                public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
//                    // ICE 후보 수집 상태 변경 시 호출됨
//                    Log.d(TAG, "onIceGatheringChange: " + iceGatheringState.toString());
//                    if(iceGatheringState == PeerConnection.IceGatheringState.COMPLETE){
//
//
//                        String localSDP = String.valueOf(peerConnection.getLocalDescription().description);
//                        Log.i("info","localSDP : "+localSDP);
//                        WebRTCClient.SendOfferTask sendOfferTask = new SendOfferTask(localSDP);
//                        sendOfferTask.execute();
//                    }
//                }
//
//                @Override
//                public void onAddStream(MediaStream mediaStream) {
//                    // 미디어 스트림이 추가되었을 때 호출됨
//                    Log.d(TAG, "onAddStream: " + mediaStream.toString());
//                }
//
//                @Override
//                public void onRemoveStream(MediaStream mediaStream) {
//                    // 미디어 스트림이 제거되었을 때 호출됨
//                    Log.d(TAG, "onRemoveStream: " + mediaStream.toString());
//                }
//
//                @Override
//                public void onDataChannel(DataChannel dataChannel) {
//                    // 데이터 채널이 생성되었을 때 호출됨
//                    Log.d(TAG, "onDataChannel Event!!");
//                    Log.d(TAG, "onDataChannel: " + dataChannel.label());
//                    Log.d(TAG, "onDataChannel: " + dataChannel.id());
//                    Log.d(TAG, "onDataChannel: " + dataChannel.state());
//
//                    // 데이터 채널 처리 작업 수행
//                    // dataChannelObserver를 등록하여 데이터 채널 이벤트 처리
//                    dataChannel.registerObserver(dataChannelObserver);
//                    dataChannel2 = peerConnection.createDataChannel("chat2222", new DataChannel.Init());
//                    dataChannel2.registerObserver(new DataChannel.Observer() {
//                        @Override
//                        public void onBufferedAmountChange(long l) {
//
//                        }
//
//                        @Override
//                        public void onStateChange() {
//                            Log.d(TAG, "onDataChannel222 statechange: " + dataChannel2.state());
//                            Log.d(TAG, "onDataChannel222 statechange: " + dataChannel2.label());
//                            Log.d(TAG, "onDataChannel222 statechange: " + dataChannel2);
//                            Log.d(TAG, "onDataChannel111 statechange: " + dataChannel.state());
//                            Log.d(TAG, "onDataChannel111 statechange: " + dataChannel.label());
//                            Log.d(TAG, "onDataChannel111 statechange: " + dataChannel);
//
////                            if(dataChannel2.state() == DataChannel.State.OPEN){
////                                String message = "Hello, World!";
////                                DataChannel.Buffer buffer = new DataChannel.Buffer(ByteBuffer.wrap(message.getBytes()), false);
////                                dataChannel2.send(buffer);
////                            }
//                        }
//
//                        @Override
//                        public void onMessage(DataChannel.Buffer buffer) {
//
//                        }
//                    });
//
//                }
//
//                @Override
//                public void onRenegotiationNeeded() {
//                    // renegotiation이 필요할 때 호출됨
//                    Log.d(TAG, "onRenegotiationNeeded");
//                    // renegotiation 작업 수행
//                }
//
//                @Override
//                public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
//                    Log.d(TAG, "onAddTrack");
//                }
//            });
//        }
//
//        public void createDataChannel() {
////            // DataChannel 초기화 설정
//            DataChannel.Init dataChannelInit = new DataChannel.Init();
//            dataChannelInit.id = 1;
//            dataChannelInit.negotiated = true;
//            dataChannelInit.ordered = true;
//            dataChannelInit.maxRetransmits = -1;
//
//            // DataChannel 생성
//            try {
//                dataChannel = peerConnection.createDataChannel("chat123", dataChannelInit);
//                dataChannel.registerObserver(dataChannelObserver);
//
//                Log.i("INFO","createDataChannel success : "+dataChannel.id());
//            } catch (Exception e) {
//                Log.e("ERROR!","createDataChannel ERROR : "+e);
//            }
//        }
//
//        public void sendMessage(String message) {
//            // 데이터 채널을 통해 메시지 전송
//            DataChannel.Buffer buffer = new DataChannel.Buffer(ByteBuffer.wrap(message.getBytes()), false);
//            dataChannel.send(buffer);
//        }
//
//        // send offer
//        public class SendOfferTask extends AsyncTask<Void, Void, String> {
//
//            private static final String TAG = "SendOfferTask";
//            private static final String TARGET_URL = "https://4ac9-165-132-105-190.ngrok-free.app/offer"; // 대상 URL
//
//            private final String localSDP;
//
//            public SendOfferTask(String localSDP) {
//                this.localSDP = localSDP;
//            }
//
//            @Override
//            protected String doInBackground(Void... voids) {
//                HttpURLConnection connection = null;
//                BufferedReader reader = null;
//                try {
//                    // 연결 설정
//                    URL url = new URL(TARGET_URL);
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("POST");
//                    connection.setRequestProperty("Content-Type", "application/json");
//                    connection.setDoOutput(true);
//
//                    // 전송할 데이터 생성
//                    JSONObject requestData = new JSONObject();
//                    requestData.put("sdp", localSDP);
//                    requestData.put("type", "offer");
//
//                    // 데이터 전송
//                    OutputStream outputStream = connection.getOutputStream();
//                    outputStream.write(requestData.toString().getBytes());
//                    outputStream.flush();
//
//                    // 응답 받기
//                    int responseCode = connection.getResponseCode();
//                    Log.i("WebRTC INFO", "responseCode"+responseCode);
//                    if (responseCode == HttpURLConnection.HTTP_OK) {
//                        // 응답 성공
//                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                        StringBuilder response = new StringBuilder();
//                        String line;
//                        while ((line = reader.readLine()) != null) {
//                            response.append(line);
//                        }
//                        return response.toString();
//                    } else {
//                        // 응답 실패
//                        return null;
//                    }
//                } catch (IOException | JSONException e) {
//                    e.printStackTrace();
//                    return null;
//                } finally {
//                    // 연결 종료 및 리소스 해제
//                    if (connection != null) {
//                        connection.disconnect();
//                    }
//                    if (reader != null) {
//                        try {
//                            reader.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//
//            @Override
//            protected void onPostExecute(String response) {
//                if (response != null) {
//                    // 응답 처리
//                    try {
//                        JSONObject responseJson = new JSONObject(response);
//                        Log.i("INFO","res:"+responseJson.toString());
//                        String answerSDP = responseJson.getString("sdp");
//                        // Answer SDP 처리
//                        peerConnection.setRemoteDescription(new SdpObserver() {
//                            @Override
//                            public void onCreateSuccess(SessionDescription sessionDescription) {
//                                // Now you can get the local description
//                                Log.d(TAG, "onCreateSuccess-setRemoteDescription: " + sessionDescription.type);
//                                SessionDescription remoteDescription = peerConnection.getRemoteDescription();
//
//                                if (remoteDescription != null) {
//                                    // Local description is available
//                                    // You can access it using localDescription.description
//                                    Log.d(TAG, "Remote Description: " + remoteDescription.description);
//                                } else {
//                                    // Local description is not available
//                                    Log.d(TAG, "Remote Description is null");
//                                }
//                            }
//
//                            @Override
//                            public void onSetSuccess() {
//                                Log.d(TAG, "onSetSuccess-setRemoteDescription");
//                            }
//
//                            @Override
//                            public void onCreateFailure(String s) {
//                                Log.e(TAG, "onCreateFailure-setRemoteDescription"+s);
//                            }
//
//                            @Override
//                            public void onSetFailure(String s) {
//                                Log.e(TAG, "onSetFailure-setRemoteDescription"+s);
//                            }
//                        },new SessionDescription(SessionDescription.Type.ANSWER, answerSDP));
//                        Log.i("info","remoteSDP : "+peerConnection.getRemoteDescription().description);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    // 응답 실패 처리
//                    Log.e(TAG, "Failed to send offer");
//                }
//            }
//        }
//    }
//
//    private void bindViews() {
//        mCapturePlayerView = findViewById(R.id.player_capture);
//        mCapturePlayerView.setLifecycle(getLifecycle());
//
//        mBtnSwitch = findViewById(R.id.btn_start);
//        mBtnSwitch.setOnClickListener(v -> {
//            if (mBtnSwitch.isChecked()) {
//                InstaCameraManager.getInstance().startPreviewStream();
//            } else {
//                InstaCameraManager.getInstance().closePreviewStream();
//            }
//        });
//
//    }
//
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (isFinishing()) {
//            // Auto close preview after page loses focus
//            InstaCameraManager.getInstance().setPreviewStatusChangedListener(null);
//            InstaCameraManager.getInstance().closePreviewStream();
//            mCapturePlayerView.destroy();
//        }
//    }
//
//    @Override
//    public void onOpening() {
//        // Preview Opening
//        mBtnSwitch.setChecked(true);
//        // If you want to set your custom surface, do like this.
//        createSurfaceView();
//    }
//
//    @Override
//    public void onOpened() {
//        // Preview stream is on and can be played
//        InstaCameraManager.getInstance().setStreamEncode();
//        mCapturePlayerView.setPlayerViewListener(new PlayerViewListener() {
//            @Override
//            public void onLoadingFinish() {
//                InstaCameraManager.getInstance().setPipeline(mCapturePlayerView.getPipeline());
//            }
//
//            @Override
//            public void onReleaseCameraPipeline() {
//                InstaCameraManager.getInstance().setPipeline(null);
//            }
//        });
//        mCapturePlayerView.prepare(createParams());
//        mCapturePlayerView.play();
//        mCapturePlayerView.setKeepScreenOn(true);
//    }
//
//    private CaptureParamsBuilder createParams() {
//        CaptureParamsBuilder builder = new CaptureParamsBuilder()
//                .setCameraType(InstaCameraManager.getInstance().getCameraType())
//                .setMediaOffset(InstaCameraManager.getInstance().getMediaOffset())
//                .setMediaOffsetV2(InstaCameraManager.getInstance().getMediaOffsetV2())
//                .setMediaOffsetV3(InstaCameraManager.getInstance().getMediaOffsetV3())
//                .setCameraSelfie(InstaCameraManager.getInstance().isCameraSelfie())
//                .setGyroTimeStamp(InstaCameraManager.getInstance().getGyroTimeStamp())
//                .setBatteryType(InstaCameraManager.getInstance().getBatteryType())
//                .setRenderModelType(CaptureParamsBuilder.RENDER_MODE_PLANE_STITCH) // RENDER_MODE_PLANE_STITCH, RENDER_MODE_AUTO
//
//                //.setScreenRatio(1, 1)
//                //.setResolutionParams(3840,1920,30)
//                //.setCameraRenderSurfaceInfo(mImageReader.getSurface(), mImageReader.getWidth(), mImageReader.getHeight());
//                .setCameraRenderSurfaceInfo(mImageReader.getSurface(), 3840,1920);
//        Log.i("info","mImageReader width x height"+mImageReader.getWidth()+"x"+mImageReader.getHeight());
//
//        return builder;
//    }
//
//    private void createSurfaceView() {
//        if (mImageReader != null) {
//            return;
//        }
//
//        File dir = new File(getExternalCacheDir(), "preview_jpg");
//        dir.mkdirs();
//        mImageReaderHandlerThread = new HandlerThread("camera render surface");
//        mImageReaderHandlerThread.start();
//
//        mImageReaderHandler = new Handler(mImageReaderHandlerThread.getLooper());
//        mImageReader = ImageReader.newInstance(mCapturePlayerView.getWidth(), mCapturePlayerView.getHeight(), PixelFormat.RGBA_8888, 1);
//        Log.i(TAG,"mCapturePlayerView SIZE : "+mCapturePlayerView.getWidth()+mCapturePlayerView.getHeight());
//
//
//        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
//            long startTime = System.currentTimeMillis();
//            @Override
//            public void onImageAvailable(ImageReader reader) {
//                Image image = reader.acquireLatestImage();
//                Log.i(TAG,"onImageAvailable @@@@@@@@@");
//                Log.i(TAG, "image format " + image.getFormat());
//                Log.i(TAG, "getWidth " + image.getWidth());
//                Log.i(TAG, "get height " + image.getHeight());
//                Log.i(TAG, "timestamp " + image.getTimestamp());
//
//                Log.i(TAG, "Time : "+(System.currentTimeMillis() - startTime));
//                //Toast.makeText(getApplicationContext(),String.valueOf((System.currentTimeMillis() - startTime)) , Toast.LENGTH_SHORT).show();
//                startTime = System.currentTimeMillis();
//
//                int planeCount = image.getPlanes().length;
//
//
//                Log.i(TAG, "plane count " + planeCount);
//                Image.Plane plane = image.getPlanes()[0];
//                int pixelStride = plane.getPixelStride();
//                int rowStride = plane.getRowStride();
//                int rowPadding = rowStride - pixelStride * image.getWidth();
//                Log.i(TAG, " plane getPixelStride " + pixelStride + " getRowPadding " + rowPadding);
//
//
//                //Bitmap bitmap = Bitmap.createBitmap(image.getWidth() + rowPadding / pixelStride, image.getHeight(), Bitmap.Config.ARGB_8888);
//                Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
//                bitmap.copyPixelsFromBuffer(plane.getBuffer());
//
//                Log.i("info","getRowBytes"+bitmap.getRowBytes());
//
//                String filePath = dir.getAbsolutePath() + "/" + image.getTimestamp() + ".png";
//                File imageFile = new File(filePath);
//
//                FileOutputStream os = null;
//                try {
//                    os = new FileOutputStream(imageFile);
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
//                    Log.i(TAG, "path " + filePath);
//                    try {
//                        os.flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//
//                image.close();
//            }
//        }, mImageReaderHandler);
//    }
//
//    @Override
//    public void onIdle() {
//        // Preview Stopped
//        mCapturePlayerView.destroy();
//        mCapturePlayerView.setKeepScreenOn(false);
//
//        // Stop the thread and destroy the ImageReader
//        if (mImageReaderHandlerThread != null) {
//            mImageReaderHandlerThread.quit();
//            mImageReaderHandlerThread = null;
//            mImageReaderHandler = null;
//            mImageReader = null;
//        }
//    }
//
//    @Override
//    public void onError() {
//        // Preview Failed
//        mBtnSwitch.setChecked(false);
//    }
//
//
//    @Override
//    public void onVideoData(VideoData videoData) {
//        Log.i("info","videoData.data  : "+videoData.data.toString());
//
//    }
//
//
//}
