#version 120

// Eyes in the Shadows: a jumpscare where the eyes are glitched. The picture breaks up in white glitches (torn bands
// smeared into glowing streaks, jumping blocks, split colors, thin interference lines, static) with flickering white
// edges, more often and harder as Intensity grows. The glitch of a Dimensional Doors Monolith's heavy aggro, in white.

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;

uniform vec2 OutSize;
uniform float Intensity;
uniform float Clock;

float hash(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

void main() {
    vec2 uv = texCoord;
    float frame = floor(Clock * 24.0);

    // Most frames glitch at full strength
    float burst = step(1.0 - (0.1 + 0.6 * Intensity), hash(vec2(frame, 1.7))) * Intensity;

    // Torn bands slide sideways
    float row = floor(uv.y * 32.0 + hash(vec2(frame, 3.1)) * 4.0);
    float torn = step(0.65, hash(vec2(row, frame)));
    uv.x += torn * (hash(vec2(row, frame + 9.0)) - 0.5) * 0.2 * burst;

    // Blocks of the picture jump, as a broken video stream's do
    vec2 cell = floor(uv * vec2(24.0, 14.0));
    float jumps = step(0.85, hash(cell + vec2(frame * 0.37, frame)));
    uv += jumps * burst * (vec2(hash(cell + 5.0 + frame), hash(cell + 7.0 + frame)) - 0.5) * 0.14;

    // The colors split apart, most at the edges
    vec2 centered = texCoord - 0.5;
    float split = (0.004 * length(centered) * 2.0 + 0.018 * burst) * Intensity;
    vec3 color = vec3(
        texture2D(DiffuseSampler, uv + vec2(split, 0.0)).r,
        texture2D(DiffuseSampler, uv).g,
        texture2D(DiffuseSampler, uv - vec2(split, 0.0)).b);

    // Some torn bands smear sideways into glowing streaks
    float streak = torn * step(0.6, hash(vec2(row * 1.3, frame + 2.0))) * burst;
    if (streak > 0.0) {
        vec3 smear = vec3(0.0);
        for (int i = 0; i < 8; i++) {
            smear += texture2D(DiffuseSampler, uv + vec2((float(i) - 3.5) * 0.012, 0.0)).rgb;
        }
        smear /= 8.0;
        color = mix(color, smear, streak * 0.7) + smear * smear * streak * 0.9;
    }

    // Thin white interference lines, a few a frame, each a couple of pixels with a soft glow
    float lines = 0.0;
    for (int i = 0; i < 3; i++) {
        float on = step(0.4, hash(vec2(frame, 20.0 + float(i))));
        float y = hash(vec2(frame * 1.7, 30.0 + float(i)));
        float d = (texCoord.y - y) * OutSize.y;
        lines += on * exp(-d * d / 2.0) * (0.6 + 0.4 * hash(vec2(frame, 40.0 + float(i))));
    }
    color += vec3(lines) * burst * 0.8;

    // White edges, flickering: the outer part of the screen, the corners most
    float flicker = 0.5 + 0.5 * hash(vec2(frame, 11.0));
    float edge = smoothstep(0.6, 1.4, length(centered * 2.0));
    color = mix(color, vec3(0.95), edge * Intensity * (0.3 + 0.5 * flicker));

    // Thick static, and a washed-out white cast over bursts
    float grain = hash(texCoord * OutSize + vec2(frame * 13.0, frame * 7.0));
    color += (grain - 0.5) * (0.06 + 0.3 * burst) * Intensity;
    float grey = dot(color, vec3(0.299, 0.587, 0.114));
    color = mix(color, vec3(grey) * 0.7 + 0.35, burst * 0.25);

    gl_FragColor = vec4(clamp(color, 0.0, 1.0), 1.0);
}
