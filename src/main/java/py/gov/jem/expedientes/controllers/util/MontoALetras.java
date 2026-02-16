/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.controllers.util;

/**
 *
 * @author juanr
 */
public class MontoALetras {

    public static String valor = "";
    private static String Centena[] = {
        "Cien", "Docientos",
        "Trecientos", "Cuatrocientos",
        "Quinientos", "Seicientos",
        "Setecientos", "Ochocientos",
        "Novecientos", "Mil", "Un Millon",
        "Millones", "Un Billon", "Billones"

    };
    private static String Decena[] = {
        "Veinte", "Treinta", "Cuarenta", "Cincuenta",
        "Sesenta", "Setenta", "Ochenta", "Noventa"
    };
    private static String Unidad[] = {
        "Cero", "Un", "Dos", "Tres",
        "Cuatro", "Cinco", "Seis", "Siete",
        "Ocho", "Nueve", "Diez", "Once", "Doce",
        "Trece", "Catorce", "Quince", "Dieciseis", "Diecisiete", "Dieciocho",
        "Diecinueve", "Veinte"
    };

    public static String toLetras(long Numero) {
        if (Numero >= 0) {
            return (resolverIntervalo(Numero));
        } else {
            return (" Menos " + resolverIntervalo(Numero * -1));
        }
    }

    private static String resolverIntervalo(long Numero) {
        if (Numero >= 0 & Numero <= 20) {
            return valor = getUnidad(Numero);
        }
        if (Numero >= 21 & Numero <= 99) {
            return valor = getDecena(Numero);
        }
        if (Numero >= 100 & Numero <= 999) {
            return valor = getCentena(Numero);
        }
        if (Numero >= 1000 & Numero <= 999999) {
            return valor = getMil(Numero);
        }
        if (Numero >= 1000000 & Numero <= 999999999) {
            return valor = getMillon(Numero);
        }
        if (Numero >= 1000000000 & Numero <= 2000000000) {
            return valor = getBillon(Numero);
        }
        return "<<El numero esta fuera del rango>>";
    }

    private static String getUnidad(long Numero) {
        String aux = " ";
        for (int p = 0; p <= 20; p++) {
            if (Numero == p) {
                aux = Unidad[p] + " ";
                return aux;
            }
        }
        return " ";
    }

    private static String getDecena(long Numero) {
        String aux = " ";
        long pf = Numero % 10;
        long pi = (Numero / 10);
        int p = 0;
        boolean sal = false;
        while (p <= 8 & sal == false) {
            if (pi == p + 2) {
                aux = Decena[p];
                sal = true;
            }
            p++;
        }
        if (pf == 0) {
            return aux + " ";
        }
        if (Numero > 20 & Numero < 30) {
            return (aux + " y " + getUnidad(pf) + "  ");
        }
        return aux + " Y " + getUnidad(pf) + " ";
    }

    private static String getCentena(long Numero) {
        String aux = " ", aux2 = " ";
        long pf = Numero % 100;
        long pi = (Numero / 100);
        int p = 0;
        boolean sal = false;
        while (p <= 10 & sal == false) {
            if (pi == p + 1) {
                aux = Centena[p];
                sal = true;
            }
            p++;
        }
        if (pf == 0) {
            return aux;
        }
        if (pf < 21) {
            aux2 = getUnidad(pf);
        } else {
            aux2 = getDecena(pf);
        }
        if (Numero < 200) {
            return (aux + "to " + aux2 + " ");
        } else {
            return (aux + " " + aux2 + " ");
        }
    }

    private static String getMil(long Numero) {
        String aux = " ", aux2 = " ";
        long pf = Numero % 1000;
        long pi = (Numero / 1000);
        long p = 0;
        if (Numero == 1000) {
            return "Mil";
        }
        if (Numero > 1000 & Numero < 1999) {
            aux = Centena[9] + " ";
        } else {
            aux = resolverIntervalo(pi) + Centena[9] + " ";
        }
        if (pf != 0) {
            return (aux + resolverIntervalo(pf) + " ");
        }
        return aux;
    }

    private static String getMillon(long Numero) {
        String aux = " ", aux2 = " ";
        long pf = Numero % 1000000;
        long pi = (Numero / 1000000);
        long p = 0;
        if (Numero >= 1000000 & Numero < 1999999) {
            aux = Centena[10] + " ";
        } else {
            aux = resolverIntervalo(pi) + Centena[11] + " ";
        }
        if (pf != 0) {
            return (aux + resolverIntervalo(pf) + " ");
        }
        return aux;
    }

    private static String getBillon(long Numero) {
        String aux = " ", aux2 = " ";
        long pf = Numero % 1000000000;
        long pi = (Numero / 1000000000);
        long p = 0;
        if (Numero > 1000000000 & Numero < 1999999999) {
            aux = Centena[12] + " ";
        } else {
            aux = resolverIntervalo(pi) + Centena[13] + " ";
        }
        if (pf != 0) {
            return (aux + resolverIntervalo(pf) + " ");
        }
        return aux;
    }
}
