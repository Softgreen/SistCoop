define(['./module'], function (services) {
  'use strict';
  services.factory("NumLetrasJ", function () {

    var Tipo = ['Pronombre', 'DetMasculino', 'DetFemenino'];

    var nombresIrregulares = ["", "uno", "dos", "tres",
      "cuatro", "cinco", "seis", "siete", "ocho", "nueve", "diez",
      "once", "doce", "trece", "catorce", "quince", "dieciseis",
      "diecisiete", "dieciocho", "diecinueve", "veinte", "veintiuno",
      "veintidos", "veintitres", "veinticuatro", "veinticinco",
      "veintiseis", "veintisiete", "veintiocho", "veintinueve"];
    var decenas = ["", "", "", "treinta", "cuarenta",
      "cincuenta", "sesenta", "setenta", "ochenta", "noventa"];
    var centenas = ["", "", "doscient", "trescient",
      "cuatrocient", "quinient", "seiscient", "setecient", "ochocient",
      "novecient"];
    var nombreMillonesSingular = ["", "millon", "billon",
      "trillon", "cuatrillon"];
    var nombreMillonesPlural = ["", "millones",
      "billones", "trillones", "cuatrillones"];


    var Convierte1 = function (i, t) {
      var r = "";
      if ((i == 1) && (t == Tipo[1])) {
        r = "un";
      } else if ((i == 1) && (t == Tipo[2])) {
        r = "una";
      } else {
        r = nombresIrregulares[i];
      }
      return r;
    };

    var Convierte2 = function (i, t) {
      var r = "";
      if (i <= 9) {
        r = r.concat(Convierte1(i, t));
      } else if (i == 21 && t != Tipo[0]) {
        if (t == Tipo[1]) {
          r = r.concat("veintiun");
        } else {
          r = r.concat("veintiuna");
        }
      } else if (i >= 10 && i <= 29) {
        r = r.concat(nombresIrregulares[i]);
      } else {
        r = r.concat(decenas[Math.floor(i / 10)]);
        if (i % 10 != 0) {
          r = r.concat(" y ");
          r = r.concat(Convierte1(i % 10, t));
        }
      }
      return r.toString();
    };

    var Convierte3 = function (i, t) {
      var r = "";
      if (i <= 99) {
        r = r.concat(Convierte2(i, t));
      } else if (i == 100) {
        r = r.concat("cien");
      } else {
        if (i >= 101 && i <= 199) {
          r = r.concat("ciento");
        } else {
          r = r.concat(centenas[Math.floor(i / 100)]);
          r = r.concat(t == Tipo[2] ? "as" : "os");
        }
        if (i % 100 > 0) {
          r = r.concat(" ");
          r = r.concat(Convierte2(i % 100, t));
        }
      }
      return r.toString();
    };

    var Convierte6 = function (i, t) {
      var r = "";
      var tresPrimeros = Math.floor(i / 1000);
      var tresUltimos = i % 1000;
      if (tresPrimeros == 1) {
        r = r.concat("mil");
      } else if (tresPrimeros >= 2) {
        if (t == Tipo[0])
          r = r.concat(Convierte3(tresPrimeros, Tipo[1]));
        else
          r = r.concat(Convierte3(tresPrimeros, t));
        r = r.concat(" mil");
      }

      if (tresUltimos > 0) {
        if (tresPrimeros > 0) {
          r = r.concat(" ");
        }
        r = r.concat(Convierte3(tresUltimos, t));
      }
      return r.toString();
    };

    var EsNumero = function (s) {
      var resultado = true;
      var contador = 0;
      var longitud = s.length;
      while (resultado && contador < longitud) {
        if (s.charAt(contador) < '0' || s.charAt(contador) > '9') {
          resultado = false;
        }
        contador++;
      }
      return resultado;
    };

    var ConvierteTodo = function (s, t) {
      var resultado = "";
      var cuenta = s.length;
      var cuentamillones = 0;
      while (cuenta > 0) {
        var inicio = (cuenta - 6 >= 0) ? (cuenta - 6) : 0;
        var longitud = (6 > cuenta) ? cuenta : 6;
        var stemp = s.substring(inicio, inicio + longitud);
        var i6 = parseInt(stemp);
        if (cuentamillones > 0 && i6 > 0) {
          if (resultado.length > 0) {
            //resultado.insert(0, " ");
            resultado = " " + resultado;
          }
          if (i6 > 1) {
            //resultado.insert(0, nombreMillonesPlural[cuentamillones]);
            resultado = nombreMillonesPlural[cuentamillones] + resultado;
          } else {
            //resultado.insert(0, nombreMillonesSingular[cuentamillones]);
            resultado = nombreMillonesSingular[cuentamillones] + resultado;
          }
          //resultado.insert(0, " ");
          resultado = " " + resultado;
        }
        //resultado.insert(0, Convierte6(i6, t));
        resultado = Convierte6(i6, t) + resultado;
        if (cuentamillones == 0) {
          t = Tipo[1];
        }
        cuentamillones++;
        cuenta -= 6;
      }
      return resultado.toString();
    };

    return {
      Convierte: function (s, t) {
        if (angular.isUndefined(t)) {
          t = Tipo[0];
        }

        var resultado = "";
        s = s.trim();
        if (s.length > 30) {
          resultado = "Demasiado grande. Llego hasta 30 cifras";
        } else if (!EsNumero(s)) {
          resultado = "La cadena no est� formada s�lo por n�meros";
        } else {
          resultado = ConvierteTodo(s, t);
        }
        return resultado;
      }
    };

  })
});
