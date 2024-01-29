package lc.deathmessages;

import org.bukkit.Sound;

public class LCSound {

    private Sound sonido;
    private float volumen;
    private float pitch;

    public LCSound(Sound sonido, float volumen, float pitch) {
        this.sonido = sonido;
        this.volumen = volumen;
        this.pitch = pitch;
    }

    public Sound getSonido() {
        return sonido;
    }

    public void setSonido(Sound sonido) {
        this.sonido = sonido;
    }

    public float getVolumen() {
        return volumen;
    }

    public void setVolumen(float volumen) {
        this.volumen = volumen;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
