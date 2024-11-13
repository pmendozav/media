package androidx.media3.demo.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.VideoSize;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.decoder.DecoderInputBuffer;
import androidx.media3.exoplayer.DecoderCounters;
import androidx.media3.exoplayer.DecoderReuseEvaluation;
import androidx.media3.exoplayer.DefaultRenderersFactory;
import androidx.media3.exoplayer.ExoPlaybackException;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.exoplayer.audio.AudioRendererEventListener;
import androidx.media3.exoplayer.audio.AudioSink;
import androidx.media3.exoplayer.mediacodec.MediaCodecAdapter;
import androidx.media3.exoplayer.mediacodec.MediaCodecInfo;
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector;
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil;
import androidx.media3.exoplayer.video.MediaCodecVideoRenderer;
import androidx.media3.exoplayer.video.VideoRendererEventListener;
import java.util.ArrayList;
import java.util.List;




/**
 * helper class used to combine 2 VideoRendererEventListener instances
 */
@UnstableApi
@SuppressLint("UnsafeOptInUsageError")
class CompositeVideoRendererEventListener implements VideoRendererEventListener {

  private final VideoRendererEventListener listener1;
  private final VideoRendererEventListener listener2;

  public CompositeVideoRendererEventListener(VideoRendererEventListener listener1, VideoRendererEventListener listener2) {
    this.listener1 = listener1;
    this.listener2 = listener2;
  }

  @Override
  public void onVideoEnabled(DecoderCounters counters) {
    listener1.onVideoEnabled(counters);
    listener2.onVideoEnabled(counters);
  }

  @Override
  public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs,
      long initializationDurationMs) {
    listener1.onVideoDecoderInitialized(decoderName, initializedTimestampMs,
        initializationDurationMs);
    listener2.onVideoDecoderInitialized(decoderName, initializedTimestampMs,
        initializationDurationMs);
  }

  @Override
  public void onVideoInputFormatChanged(Format format,
      @Nullable DecoderReuseEvaluation decoderReuseEvaluation) {

    listener1.onVideoInputFormatChanged(format, decoderReuseEvaluation);
    listener2.onVideoInputFormatChanged(format, decoderReuseEvaluation);
  }

  @Override
  public void onDroppedFrames(int count, long elapsedMs) {
    listener1.onDroppedFrames(count, elapsedMs);
    listener2.onDroppedFrames(count, elapsedMs);
  }

  @Override
  public void onVideoFrameProcessingOffset(long totalProcessingOffsetUs, int frameCount) {
    listener1.onVideoFrameProcessingOffset(totalProcessingOffsetUs, frameCount);
    listener2.onVideoFrameProcessingOffset(totalProcessingOffsetUs, frameCount);
  }

  @Override
  public void onVideoSizeChanged(VideoSize videoSize) {
    listener1.onVideoSizeChanged(videoSize);
    listener2.onVideoSizeChanged(videoSize);
  }

  @Override
  public void onRenderedFirstFrame(Object output, long renderTimeMs) {
    listener1.onRenderedFirstFrame(output, renderTimeMs);
    listener2.onRenderedFirstFrame(output, renderTimeMs);
  }

  @Override
  public void onVideoDecoderReleased(String decoderName) {
    listener1.onVideoDecoderReleased(decoderName);
    listener2.onVideoDecoderReleased(decoderName);
  }

  @Override
  public void onVideoDisabled(DecoderCounters counters) {
    listener1.onVideoDisabled(counters);
    listener2.onVideoDisabled(counters);
  }

  @Override
  public void onVideoCodecError(Exception videoCodecError) {
    listener1.onVideoCodecError(videoCodecError);
    listener2.onVideoCodecError(videoCodecError);
  }
}

@SuppressLint("UnsafeOptInUsageError")
class CustomVideoRendererEventListener implements
    VideoRendererEventListener {

  public CustomVideoRendererEventListener() {
    Log.d("CVideoRendererEListener", "new instance");
  }

  @Override
  public void onVideoEnabled(DecoderCounters counters) {
    Log.d("CVideoRendererEListener", "onVideoEnabled: " + counters);
  }

  @Override
  public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs,
      long initializationDurationMs) {
    Log.d("CVideoRendererEListener","onVideoDecoderInitialized: decoderName=" + decoderName + ". initializedTimestampMs=" + initializedTimestampMs + " . initializationDurationMs=" + initializationDurationMs);
  }

  @Override
  public void onVideoInputFormatChanged(Format format,
      @Nullable DecoderReuseEvaluation decoderReuseEvaluation) {
    Log.d("CVideoRendererEListener", "onVideoInputFormatChanged: format=" + format + ". decoderReuseEvaluation=" + decoderReuseEvaluation);
  }

  @Override
  public void onDroppedFrames(int count, long elapsedMs) {
    Log.d("CVideoRendererEListener", "onDroppedFrames: count=" + count + ". elapsedMs=" + elapsedMs);
  }

  @Override
  public void onVideoFrameProcessingOffset(long totalProcessingOffsetUs, int frameCount) {
    Log.d("CVideoRendererEListener", "onVideoFrameProcessingOffset: totalProcessingOffsetUs=" + totalProcessingOffsetUs + ". frameCount=" + frameCount);
  }

  @Override
  public void onVideoSizeChanged(VideoSize videoSize) {
    Log.d("CVideoRendererEListener", "onVideoSizeChanged: videoSize=" + videoSize);
  }

  @Override
  public void onRenderedFirstFrame(Object output, long renderTimeMs) {
    Log.d("CVideoRendererEListener", "onRenderedFirstFrame: output=" + output + ". renderTimeMs=" + renderTimeMs);
  }

  @Override
  public void onVideoDecoderReleased(String decoderName) {
    Log.d("CVideoRendererEListener", "onVideoDecoderReleased: decoderName=" + decoderName);
  }

  @Override
  public void onVideoDisabled(DecoderCounters counters) {
    Log.d("CVideoRendererEListener", "onVideoDisabled: counters=" + counters);
  }

  @Override
  public void onVideoCodecError(Exception videoCodecError) {
    Log.d("CVideoRendererEListener", "onVideoCodecError: videoCodecError=" + videoCodecError);
  }
}

@SuppressLint("UnsafeOptInUsageError")
class CustomMediaCodecRenderer extends MediaCodecVideoRenderer {
  final String codec_name_filtered = "OMX.amlogic.avc.decoder.awesome2";
  private String currentDecoderName;
  private boolean forceException;

  private boolean checkFilterCodec(String decoderName) {
    return forceException && decoderName != null && decoderName.contains(codec_name_filtered);
  }

  public CustomMediaCodecRenderer(
      Context context,
      MediaCodecSelector mediaCodecSelector,
      boolean enableDecoderFallback,
      Handler eventHandler,
      VideoRendererEventListener eventListener,
      long allowedVideoJoiningTimeMs,
      int maxDroppedFrameToNotify,
      boolean useForceException
  ) {
    super(context, mediaCodecSelector, allowedVideoJoiningTimeMs, enableDecoderFallback,
        eventHandler, eventListener, maxDroppedFrameToNotify);
    forceException = useForceException;
    Log.d("CMediaCodecRenderer", "new instance. enableDecoderFallback=" + enableDecoderFallback);
  }

  @Override
  protected void onCodecError(Exception codecError) {
    Log.d("CMediaCodecRenderer", "onCodecError: " + codecError);
    super.onCodecError(codecError);
  }

  @Override
  protected List<MediaCodecInfo> getDecoderInfos(
      MediaCodecSelector mediaCodecSelector, Format format, boolean requiresSecureDecoder)
      throws MediaCodecUtil.DecoderQueryException {

    Log.d("CMediaCodecRenderer", "getDecoderInfos: mediaCodecSelector=" + mediaCodecSelector + ". format=" + format + ". requiresSecureDecoder" + requiresSecureDecoder);
    List<MediaCodecInfo> decoderInfos = super.getDecoderInfos(mediaCodecSelector, format,
        requiresSecureDecoder);

    Log.d("CMediaCodecRenderer", "decoderInfo names:");
    for (MediaCodecInfo info : decoderInfos) {
      Log.d("CMediaCodecRenderer", "decoderInfos[i].name: " + info.name);
    }

    return decoderInfos;
  }

  @Override
  protected void onCodecInitialized(
      String name,
      MediaCodecAdapter.Configuration configuration,
      long initializedTimestampMs,
      long initializationDurationMs
  ) throws ExoPlaybackException {
    currentDecoderName = name;
    Log.d("CMediaCodecRenderer", "onCodecInitialized: name=" + name);

    super.onCodecInitialized(currentDecoderName, configuration, initializedTimestampMs, initializationDurationMs);

    if (checkFilterCodec(currentDecoderName)) {
        Log.d("CMediaCodecRenderer", "trying to force error for hardware decoder (onCodecInitialized)");

//      // error_1: recoverable error
     throw ExoPlaybackException.createForRenderer(new IllegalStateException("Simulated failure in hardware decoder"), codec_name_filtered, 0, null, C.FORMAT_UNSUPPORTED_SUBTYPE, true, 5001);
    }
  }

  @Override
  protected void onQueueInputBuffer(DecoderInputBuffer buffer) throws ExoPlaybackException {
    super.onQueueInputBuffer(buffer);

    if (checkFilterCodec(currentDecoderName)) {
//      Log.d("CMediaCodecRenderer", "trying to force hardware decoder error (onQueueInputBuffer)");
//      // error_2: unrecoverable error
//      throw ExoPlaybackException.createForRenderer(new IllegalStateException("Simulated failure in hardware decoder"), codec_name_filtered, 0, null, C.FORMAT_UNSUPPORTED_SUBTYPE, true, 5001);
    }
  }
}

@SuppressLint("UnsafeOptInUsageError")
class CustomRendererFactory extends DefaultRenderersFactory {
  public CustomRendererFactory(Context context) {
    super(context);

    Log.d("CustomRendererFactory", "new instance");
  }

  @Override
  protected void buildAudioRenderers(
      Context context,
      @ExtensionRendererMode int extensionRendererMode,
      MediaCodecSelector mediaCodecSelector,
      boolean enableDecoderFallback,
      AudioSink audioSink,
      Handler eventHandler,
      AudioRendererEventListener eventListener,
      ArrayList<Renderer> out
  ) {
    Log.d("CustomRendererFactory", "buildAudioRenderers. enableDecoderFallback=" + enableDecoderFallback);

    super.buildAudioRenderers(
        context,
        extensionRendererMode,
        mediaCodecSelector,
        enableDecoderFallback,
        audioSink,
        eventHandler,
        eventListener,
        out
    );
  }

  @Override
  protected void buildVideoRenderers(
      Context context,
      @ExtensionRendererMode int extensionRendererMode,
      MediaCodecSelector mediaCodecSelector,
      boolean enableDecoderFallback,
      Handler eventHandler,
      VideoRendererEventListener eventListener,
      long allowedVideoJoiningTimeMs,
      ArrayList<Renderer> out
  ) {
    Log.d("CustomRendererFactory", "buildVideoRenderers. enableDecoderFallback=" + enableDecoderFallback);

    VideoRendererEventListener customVideoRendererEventListener = new CompositeVideoRendererEventListener(eventListener, new CustomVideoRendererEventListener());

    super.buildVideoRenderers(
        context,
        extensionRendererMode,
        mediaCodecSelector,
        enableDecoderFallback,
        eventHandler,
        customVideoRendererEventListener,
        allowedVideoJoiningTimeMs,
        out
    );

    out.add(0, new CustomMediaCodecRenderer(
        context,
        mediaCodecSelector,
        true,
        eventHandler,
        customVideoRendererEventListener,
        allowedVideoJoiningTimeMs,
        DefaultRenderersFactory.MAX_DROPPED_VIDEO_FRAME_COUNT_TO_NOTIFY,
        true
    ));

    out.add(1, new CustomMediaCodecRenderer(
        context,
        mediaCodecSelector,
        true,
        eventHandler,
        customVideoRendererEventListener,
        allowedVideoJoiningTimeMs,
        DefaultRenderersFactory.MAX_DROPPED_VIDEO_FRAME_COUNT_TO_NOTIFY,
        false
    ));

    Log.d("CustomRendererFactory", "video_rendererss_list_size = " + out.size());
  }
}