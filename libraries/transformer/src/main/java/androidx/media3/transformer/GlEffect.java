/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package androidx.media3.transformer;

import android.content.Context;
import androidx.media3.common.util.UnstableApi;
import java.io.IOException;

/**
 * Interface for a video frame effect with a {@link SingleFrameGlTextureProcessor} implementation.
 *
 * <p>Implementations contain information specifying the effect and can be {@linkplain
 * #toGlTextureProcessor(Context) converted} to a {@link SingleFrameGlTextureProcessor} which
 * applies the effect.
 */
@UnstableApi
public interface GlEffect {

  /** Returns a {@link SingleFrameGlTextureProcessor} that applies the effect. */
  // TODO(b/227625423): use GlTextureProcessor here once this interface exists.
  SingleFrameGlTextureProcessor toGlTextureProcessor(Context context) throws IOException;
}
