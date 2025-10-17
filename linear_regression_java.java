import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LinearRegression {
    static class VeriSeti {
        private List<double[]> veriler;
        private List<double[]> egitimVerileri;
        private List<double[]> testVerileri;

        public VeriSeti() {
            veriler = new ArrayList<>();
            egitimVerileri = new ArrayList<>();
            testVerileri = new ArrayList<>();
        }

        public void veriOlustur(int satirSayisi) {
            Random random = new Random();
            for (int i = 0; i < satirSayisi; i++) {
                double x1 = random.nextDouble() * 100;
                double x2 = random.nextDouble() * 100;
                double x3 = random.nextDouble() * 100;
                double y = 2 * x1 + 3 * x2 - x3 + (random.nextDouble() * 20 - 10);
                double[] veri = {x1, x2, x3, y};
                veriler.add(veri);
            }
        }

        public void veriBol(double egitimOrani) {
            Collections.shuffle(veriler);
            int egitimSiniri = (int) (veriler.size() * egitimOrani);
            egitimVerileri = new ArrayList<>(veriler.subList(0, egitimSiniri));
            testVerileri = new ArrayList<>(veriler.subList(egitimSiniri, veriler.size()));
        }
    }

    public static double pearsonKorelasyon(List<Double> x, List<Double> y) {
        int n = x.size();
        double xOrt = 0, yOrt = 0;
        for (int i = 0; i < n; i++) {
            xOrt += x.get(i);
            yOrt += y.get(i);
        }
        xOrt /= n; yOrt /= n;

        double pay = 0, paydaX = 0, paydaY = 0;
        for (int i = 0; i < n; i++) {
            double xFark = x.get(i) - xOrt;
            double yFark = y.get(i) - yOrt;
            pay += xFark * yFark;
            paydaX += xFark * xFark;
            paydaY += yFark * yFark;
        }
        if (paydaX == 0 || paydaY == 0) return 0;
        return pay / (Math.sqrt(paydaX) * Math.sqrt(paydaY));
    }

    public static double[] dogrusalRegresyon(List<Double> x, List<Double> y) {
        int n = x.size();
        double xOrt = 0, yOrt = 0;
        for (int i = 0; i < n; i++) { xOrt += x.get(i); yOrt += y.get(i); }
        xOrt /= n; yOrt /= n;

        double pay = 0, payda = 0;
        for (int i = 0; i < n; i++) {
            double xFark = x.get(i) - xOrt;
            double yFark = y.get(i) - yOrt;
            pay += xFark * yFark;
            payda += xFark * xFark;
        }
        double b = payda != 0 ? pay / payda : 0;
        double a = yOrt - b * xOrt;
        return new double[]{a, b};
    }

    public static List<Double> tahminHesapla(List<Double> x, double a, double b) {
        List<Double> tahminler = new ArrayList<>();
        for (double xi : x) tahminler.add(a + b * xi);
        return tahminler;
    }

    public static double sseHesapla(List<Double> yGercek, List<Double> yTahmin) {
        double sse = 0;
        for (int i = 0; i < yGercek.size(); i++) {
            double fark = yGercek.get(i) - yTahmin.get(i);
            sse += fark * fark;
        }
        return sse;
    }

    public static void main(String[] args) {
        VeriSeti veriSeti = new VeriSeti();
        veriSeti.veriOlustur(100);
        veriSeti.veriBol(0.7);

        List<Double> x1Egitim = new ArrayList<>(), x2Egitim = new ArrayList<>(), x3Egitim = new ArrayList<>(), yEgitim = new ArrayList<>();
        for (double[] veri : veriSeti.egitimVerileri) {
            x1Egitim.add(veri[0]); x2Egitim.add(veri[1]); x3Egitim.add(veri[2]); yEgitim.add(veri[3]);
        }

        double r1 = pearsonKorelasyon(x1Egitim, yEgitim);
        double r2 = pearsonKorelasyon(x2Egitim, yEgitim);
        double r3 = pearsonKorelasyon(x3Egitim, yEgitim);

        System.out.printf("Korelasyon Katsayıları:\nr1 (x1-y): %.4f\nr2 (x2-y): %.4f\nr3 (x3-y): %.4f\n", r1, r2, r3);

        String[] degiskenler = {"x1", "x2", "x3"};
        double[] korelasyonlar = {r1, r2, r3};
        int enYuksekIndex = 0;
        double enYuksekDeger = Math.abs(korelasyonlar[0]);
        for (int i = 1; i < korelasyonlar.length; i++) {
            if (Math.abs(korelasyonlar[i]) > enYuksekDeger) { enYuksekDeger = Math.abs(korelasyonlar[i]); enYuksekIndex = i; }
        }
        System.out.printf("\nEn yüksek korelasyon: %s (%.4f)\n", degiskenler[enYuksekIndex], korelasyonlar[enYuksekIndex]);

        List<Double> xSecili;
        switch (enYuksekIndex) {
            case 0: xSecili = x1Egitim; break;
            case 1: xSecili = x2Egitim; break;
            default: xSecili = x3Egitim;
        }

        double[] katsayilar = dogrusalRegresyon(xSecili, yEgitim);
        System.out.printf("\nRegresyon Modeli: y = %.4f + %.4f * %s\n", katsayilar[0], katsayilar[1], degiskenler[enYuksekIndex]);

        List<Double> yTahminEgitim = tahminHesapla(xSecili, katsayilar[0], katsayilar[1]);
        double sseEgitim = sseHesapla(yEgitim, yTahminEgitim);
        System.out.printf("\nEğitim Verileri SSE: %.4f\n", sseEgitim);

        List<Double> x1Test = new ArrayList<>(), x2Test = new ArrayList<>(), x3Test = new ArrayList<>(), yTest = new ArrayList<>();
        for (double[] veri : veriSeti.testVerileri) {
            x1Test.add(veri[0]); x2Test.add(veri[1]); x3Test.add(veri[2]); yTest.add(veri[3]);
        }

        List<Double> xTestSecili;
        switch (enYuksekIndex) {
            case 0: xTestSecili = x1Test; break;
            case 1: xTestSecili = x2Test; break;
            default: xTestSecili = x3Test;
        }

        List<Double> yTahminTest = tahminHesapla(xTestSecili, katsayilar[0], katsayilar[1]);
        double sseTest = sseHesapla(yTest, yTahminTest);
        System.out.printf("Test Verileri SSE: %.4f\n", sseTest);
    }
}