import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;


class Reservado {

    /* tipo de datos */
    public static String tipoDatos[] = {"int", "float", "char"};
    public static String palabraReservada[] = {
    	        "int", "float", "char"
    	    };
    	    
    	    public boolean isReservado(String lexema)
    	    {
    	        for(String pal : palabraReservada)
    	        {
    	            if(pal.equalsIgnoreCase(lexema))
    	                return true;
    	        }
    	        return false;
    	    }
}
class Lexema {

    private int numero;
    private String lexema;
    private int fila;
    private String tipo;
    private String comp;
    private String td;
    public Lexema() {
    }

    public Lexema(int numero, String lexema, int fila, String tipo, String comp, String td) {
        this.numero = numero;
        this.lexema = lexema;
        this.fila = fila;
        this.tipo = tipo;
        this.comp = comp;
        this.td = td;
        verificarReservado(lexema);
    }

    public void verificarReservado(String texto) {
        for (String item : Reservado.tipoDatos) {
            if (item.equalsIgnoreCase(texto)) {
                this.tipo = "TIPO DE DATO";
                return;
            }
        }
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }
    public String getTD() {
        return td;
    }

    public void setTD(String td) {
        this.td = td;
    }
    @Override
    public String toString() {
        return "Lexema{" + "numero=" + numero + ", lexema=" + lexema + ", fila=" + fila + ", tipo=" + tipo + '}';
    }

}
public class Semantico {
	 Stack<String> pila = new Stack<String>();  
     public String tok;
  public String texto;
    public int linea;
    public ArrayList<Lexema> listaLexemas; //contiene los lexemas
    public ArrayList<Error> listaErrores; //contiene los errores producidos
    public Reservado reser = new Reservado();
     public ArrayList<String> errores = new ArrayList<String>();
     String dato;
    public int contError, contLexema, contLineas;
    boolean comentario;
    boolean activarSemantico = false, existe = false, datogen = false;
    String lexemaSem = "", cadenaGen = "", var="", datofinal="";

    int contid = 0, contgen = 0;
	Stack<String> pilaSint = new Stack<String>();  
	Stack<Integer> pilaSem = new Stack<Integer>();  
	Stack<String> pilaGen = new Stack<String>(); 
	LinkedList<String> listaGen = new LinkedList<>();
	
	String valores[]= {"id", "int", "float", "char", ",", ";", "+", "-", "*", "/", "(", ")", "=", "$", "P", "Tipo", "V", "A", "Exp", "E", "Term", "T","F"};
	String edos[] = {"I0", "I1", "I2", "I3", "I4", "I5", "I6", "I7", "I8", "I9", "I10", "I11", "I12", "I13", "I14", "I15", "I16", "I17", "I18", "I19", "I20", "I21", "I22", "I23", "I24", "I25", "I26", "I27", "I28", "I29", "I30", "I31", "I32", "I33", "I34", "I35", "I36"};
	
	
	String tabla[][] = {
			{"I7", "I4", "I5", "I6", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "I1", "I2", "@", "I3", "@", "@", "@", "@", "@"},
			{"@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "P0", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"I8", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "P2", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"P3", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"P4", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"P5", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "I9", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"@", "@", "@", "@", "I11", "I12", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "I10", "@", "@", "@", "@", "@", "@"},
			{"I16", "@", "@", "@", "@", "@", "@", "@", "@", "@", "I17", "@", "@", "@", "@", "@", "@", "@", "I13", "@", "I14", "@", "I15"},
			{"@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "P1", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"I18", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"I7", "I4", "I5", "I6", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "I19", "I2", "@", "I3", "@", "@", "@", "@", "@"},
			{"@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "P8", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"@", "@", "@", "@", "P12", "@", "I21", "I22", "@", "@", "@", "@", "@", "P12", "@", "@", "@", "@", "@", "I20", "@", "@", "@"},
			{"@", "@", "@", "@", "@", "@", "P16", "P16", "I24", "I25", "@", "P16", "@", "P16", "@", "@", "@", "@", "@", "@", "@", "I23", "@"},
			{"@", "@", "@", "@", "@", "@", "P17", "P17", "P17", "P17", "@", "P17", "@", "P17", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"I16", "@", "@", "@", "@", "@", "@", "@", "@", "@", "I17", "@", "@", "@", "@", "@", "@", "@", "I26", "@", "I14", "@", "I15"},
			{"@", "@", "@", "@", "I11", "I12", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "I27", "@", "@", "@", "@", "@", "@"},
			{"@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "P7", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "P9", "@", "P9", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"I16", "@", "@", "@", "@", "@", "@", "@", "@", "@", "I17", "@", "@", "@", "@", "@", "@", "@", "@", "@", "I28", "@", "I15"},
			{"I16", "@", "@", "@", "@", "@", "@", "@", "@", "@", "I17", "@", "@", "@", "@", "@", "@", "@", "@", "@", "I29", "@", "I15"},
			{"@", "@", "@", "@", "@", "@", "P13", "P13", "@", "@", "@", "P13", "@", "P13", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"I16", "@", "@", "@", "@", "@", "@", "@", "@", "@", "I17", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "I30"},
			{"I16", "@", "@", "@", "@", "@", "@", "@", "@", "@", "I17", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "I31"},
			{"@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "I32", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "P6", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"@", "@", "@", "@", "@", "@", "I21", "I22", "@", "@", "@", "P12", "@", "P12", "@", "@", "@", "@", "@", "I33", "@", "@", "@"},
			{"@", "@", "@", "@", "@", "@", "I21", "I22", "@", "@", "@", "P12", "@", "P12", "@", "@", "@", "@", "@", "I34", "@", "@", "@"},
			{"@", "@", "@", "@", "@", "@", "P16", "P16", "I24", "I25", "@", "P16", "@", "P16", "@", "@", "@", "@", "@", "@", "@", "I35", "@"},
			{"@", "@", "@", "@", "@", "@", "P16", "P16", "I24", "I25", "@", "P16", "@", "P16", "@", "@", "@", "@", "@", "@", "@", "I36", "@"},
			{"@", "@", "@", "@", "@", "@", "P18", "P18", "P18", "P18", "@", "P18", "@", "P18", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "P10", "@", "P10", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "@", "P11", "@", "P11", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"@", "@", "@", "@", "@", "@", "P14", "P14", "@", "@", "@", "P14", "@", "P14", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			{"@", "@", "@", "@", "@", "@", "P15", "P15", "@", "@", "@", "P15", "@", "P15", "@", "@", "@", "@", "@", "@", "@", "@", "@"},
			};
	int tablasem[][] = {
			{1,2,-1},
			{2,2,-1},
			{-1,-1,3}};
	public void Sintactico()
	{
		pilaSint.clear();
		pilaSint.push("$");
		pilaSint.push("I0");
	}
	public void Analisis(String token)
	{	
			System.out.println(pilaSint);
			System.out.println("Pila semántica: "+pilaSem);
		    int col = BuscarValores(token);
		    int ren = BuscarEdos(pilaSint.peek());
		    	String vtabla = tabla[ren][col];
		    	if(token.equals("$") && vtabla.equals("P0"))
		    	{
		    		if(VerificarFinalPila())
		    		{
			    		System.out.println("Se acepta la cadena.");
			    		listaGen.add(var+"="+"V1"+";");
			    		MostrarGeneracion();
		    		}
		    		else
		    			System.out.println("Error semántico en linea "+ linea+". No coinciden los tipos en la asignación.");
		    	}
		    	else if(vtabla.startsWith("I"))
		    	{
		    		pilaSint.push(token);
		    		pilaSint.push(vtabla);
		    		if(activarSemantico)
		    			IdSem(token);
		    	
		    		
		    	}
		    	else if(vtabla.startsWith("P"))
		    	{
		    		if(vtabla.equalsIgnoreCase("P10") || vtabla.equalsIgnoreCase("P11") || vtabla.equalsIgnoreCase("P14") || vtabla.equalsIgnoreCase("P15"))
		    		{
		    			if(pilaSem.size()>1)
		    			{
			    			int T1 = pilaSem.pop();
			    			int T2 = pilaSem.pop();
			    			T1 = tablasem[T1-1][T2-1];
			    			if(T1 != -1)
			    			{
			    				pilaSem.push(T1);
			    			}
			    			else
			    			{
			    				System.out.println("Error semántico en linea "+ linea+". No coinciden los tipos.");
			    				return;
			    			}
		    			}
		    			else
		    			{
		    				System.out.println("Error semántico en linea "+ linea+". No hubo declaración de variable.");
		    				return;
		    			}
		    			String signo = "";
		    			switch(vtabla)
		    			{
		    			case "P10":
		    				signo = "+";
		    				break;
		    			case "P11":
		    				signo = "-";
		    				break;
		    			case "P14":
		    				signo = "*";
		    				break;
		    			case "P15":
		    				signo = "/";
		    				break;
		    				
		    			}
		    			cadenaGen = "V"+(contgen-1)+"="+"V"+(contgen-1)+signo+"V"+contgen+";";
		    			listaGen.add(cadenaGen);
		    			cadenaGen="";
		    			contgen--;
		    		}
		    		String reduccion[] = Reducciones(vtabla);
		    		if(reduccion[1].equals("e"))
		    		{
		    			String edoanterior = pilaSint.peek();
		    			pilaSint.push(reduccion[0]);
		    			col = BuscarValores(pilaSint.peek());
		    			ren = BuscarEdos(edoanterior);
		    			vtabla = tabla[ren][col];
		    			pilaSint.push(vtabla);
		    			Analisis(token);
		    		}
		    		else
		    		{
		    			String nr[] = reduccion[1].split(" ");
		    			for(int i=0; i<(nr.length*2); i++)
		    			{
		    				pilaSint.pop();
		    			}
		    			String edoanterior = pilaSint.peek();
		    			pilaSint.push(reduccion[0]);
		    			col = BuscarValores(pilaSint.peek());
		    			ren = BuscarEdos(edoanterior);
		    			vtabla = tabla[ren][col];
		    			pilaSint.push(vtabla);
		    			Analisis(token);
		    		}
		    	}
		  //  }
		
	    
	}
	public void IdSem(String token)
	{
		if(token.equals("id"))
		{
			if(existe)
			{
				contgen++;
				for (Lexema item : listaLexemas) {
	                if(item.getLexema().equals(lexemaSem))
	                {
	               	 dato = item.getTD();
	               	 break;
	                }
	             }
				switch(dato)
				{
				case "int":
					pilaSem.push(1);
					break;
				case "float":
					pilaSem.push(2);
					break;
				case "char":
					pilaSem.push(3);
					break;
				}
				cadenaGen = "V"+contgen+"="+lexemaSem+";";
				listaGen.add(cadenaGen);
			}
			
		}
		
	
	}
	public boolean VerificarFinalPila()
	{
		int d=0;
		switch(datofinal)
		{
		case "int":
			d = 1;
			break;
		case "float":
			d = 2;
			break;
		case "char":
			d = 3;
			break;
		}
		if(d == pilaSem.peek())
			return true;
		return false;
	}
	public String[] Reducciones(String p)
	{
		String red[] = new String[2];
		switch(p)
		{
		case "P0":
			red[0] = "P'";
			red[1] = "P";
			break;
		case "P1":
			red[0] = "P";
			red[1] = "Tipo id V";
			break;
		case "P2":
			red[0] = "P";
			red[1] = "A";
			break;
		case "P3":
			red[0] = "Tipo";
			red[1] = "int";
			break;
		case "P4":
			red[0] = "Tipo";
			red[1] = "float";
			break;
		case "P5":
			red[0] = "Tipo";
			red[1] = "char";
			break;
		case "P6":
			red[0] = "V";
			red[1] = ", id V";
			break;
		case "P7":
			red[0] = "V";
			red[1] = "; P";
			break;
		case "P8":
			red[0] = "A";
			red[1] = "id = Exp";
			break;
		case "P9":
			red[0] = "Exp";
			red[1] = "Term E";
			break;
		case "P10":
			red[0] = "E";
			red[1] = "+ Term E";
			break;
		case "P11":
			red[0] = "E";
			red[1] = "- Term E";
			break;
		case "P12":
			red[0] = "E";
			red[1] = "e";
			break;
		case "P13":
			red[0] = "Term";
			red[1] = "F T";
			break;
		case "P14":
			red[0] = "T";
			red[1] = "* F T";
			break;
		case "P15":
			red[0] = "T";
			red[1] = "/ F T";
			break;
		case "P16":
			red[0] = "T";
			red[1] = "e";
			break;
		case "P17":
			red[0] = "F";
			red[1] = "id";
			break;
		case "P18":
			red[0] = "F";
			red[1] = "( Exp )";
			break;
			
		}
		return red;
	}
	public int BuscarValores(String valor)
	 {
	       
	       for(int i=0; i<valores.length; i++)
	       {
	           if(valores[i].equalsIgnoreCase(valor))
	               return i;
	       }
	       return -1;
	}
	 public int BuscarEdos(String edo)
	   {
	       for(int i=0; i<edos.length; i++)
	       {
	           if(edos[i].equalsIgnoreCase(edo))
	               return i;
	       }
	       return -1;
	   }
  
    public Semantico() {
        this.texto = "";
        this.listaLexemas = new ArrayList<>();
        this.listaErrores = new ArrayList<>();
        this.contLineas = 0;
        this.contLexema = 0;
        this.contError = 0;
        this.linea = 0;
       
    }

    public Semantico(String texto) {
        this.texto = texto;
        this.listaLexemas = new ArrayList<>();
        this.listaErrores = new ArrayList<>();
        this.contLineas = 0;
        this.contLexema = 0;
        this.contError = 0;
        this.linea = 0;
    }

    /* metodo que analiza todo el texto caracter por caracter */
    public void analizar() {
        Sintactico();
        System.out.println(texto);
        int longitud = texto.length();
        char c;
        int estado = 0;
        String lexema = "";
        for (int i = 0; i < longitud; i++) {
            c = texto.charAt(i);
            switch (estado) {
                case 0:
                    if (c >= '0' && c <= '9') { //numeros
                        lexema = lexema + c;
                        estado = 1;
                    } else if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '$' || c == '°') { //letras
                        lexema = lexema + c;
                        estado = 2;
                    } else if (c == ';') {
                    	 dato = "";
                    	 activarSemantico = false;
                    	 datogen = false;
                        Analisis(";");
                        addLexema(";", linea, "DELIMITADOR", ";", "");
                        estado = 0;
                       
                    } else if (c == ' ') {
                        estado = 0;
                    } else if (c == '\n') {
                        estado = 0;
                        linea++;
                        contLineas++;
                    } else if (c == '=') {
                        addLexema("=", linea, "IGUAL", "=", "");
                        Analisis("=");
                        activarSemantico = true;
                        var = lexemaSem;
                        if(!BuscarDatoDeclarado(var))
                        {
                        	System.out.println("Error semántico en linea "+ linea+". No hubo declaración de variable.");
		    				return;
                        }
                        estado = 0;
                       
                    } else if (c == '+') {
                        addLexema("" + c, linea, "OPERADOR MATEMATICO", "+", "");
                        Analisis("+");
                        estado = 0;
                        
                    }else if (c == '-') {
                        addLexema("" + c, linea, "OPERADOR MATEMATICO", "-", "");
                        Analisis("-");
                        estado = 0;
                        
                    }else if (c == '*') {
                        addLexema("" + c, linea, "OPERADOR MATEMATICO", "*", "");
                        Analisis("*");
                        estado = 0;
                        
                    }else if (c == '/') {
                        addLexema("" + c, linea, "OPERADOR MATEMATICO", "/", "");
                        Analisis("/");
                        estado = 0;
                        
                    } else if (c == '(') {
                        addLexema("(", linea, "PARENTESIS ABIERTO", "(", "");
                         Analisis("(");
                        estado = 0;
                    } else if (c == ')') {
                        addLexema(")", linea, "PARENTESIS CERRADO",")", "");
                         Analisis(")");
                        estado = 0;
                    } else if (c == ',') {
                        addLexema(",", linea, "COMA", ",", "");
                         Analisis(",");
                        estado = 0;
                    } else if (c == '>' || c == '<') {
                        lexema = lexema + c;
                        estado = 6;
                    } else if (c == '"') {
                        lexema = lexema + c;
                        estado = 5;
                    } else if (c == '\t' || c == '\b') {
                        estado = 0;
                    } else {
                        contError++;
                        //listaErrores.add(new Error(contError, "Caracter raro: " + c, linea, "Lexicos"));
                        lexema = "";
                        estado = 404;
                    }
                    break;
                case 1: //numero
                    if (c >= '0' && c <= '9') {
                        lexema = lexema + c;
                        estado = 1;
                    } else if (c == '.') {
                        lexema = lexema + c;
                        estado = 4;
                    } else {
                        addLexema(lexema, linea, "NÚMERO", "num", "");
                        dato = "int";
                        Analisis("num");
                        lexema = "";
                        estado = 0;
                        i--;
                    }
                    break;
                case 2: //variables
                    if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                        lexema = lexema + c;
                        estado = 2;
                    } else if (c >= '0' && c <= '9') {
                        lexema = lexema + c;
                        estado = 2;
                    } else {
                       if(reser.isReservado(lexema))
                        {
                            addLexema(lexema, linea, "PALABRA RESERVADA", lexema, "");
                            Analisis(lexema);
                            datogen = true;
                            if(lexema.equals("int"))
                            {
                            	dato = "int";
                            }
                            else if(lexema.equals("float"))
                            {
                            	dato = "float";
                            }
                            else if(lexema.equals("char"))
                            {
                            	dato = "char";
                            }
                        
                        }
                        else
                        {
                        
                        	  for (Lexema item : listaLexemas) {
                                 if(item.getLexema().equals(lexema))
                                 {
                                	 dato = item.getTD();
                                	 existe = true;
                                	 break;
                                 }
                              }
                        	lexemaSem = lexema;
                        	addLexema(lexema, linea, "VARIABLE", "id", dato);
                            Analisis("id");
                            existe = false;
                            if(datogen)
                            {
                            	cadenaGen = dato + " " +cadenaGen + lexema+";";
                            	listaGen.add(cadenaGen);
                            	cadenaGen = "";
                            }
                        }
                       
                        lexema = "";
                        estado = 0;
                        i--;
                    }
                    break;
                case 4://numero decimal
                    if (c >= '0' && c <= '9') {
                        lexema = lexema + c;
                        estado = 4;
                    } else {
                        addLexema(lexema, linea, "float", "float", "");
                        dato = "float";
                        lexema = "";
                        estado = 0;
                        i--;
                    }
                    break;
                case 5: // cadena
                    if (c == '"') {
                        lexema = lexema + c;
                        addLexema(lexema, linea, "String", "string", "");
                         Analisis("litcad");
                        lexema = "";
                        estado = 0;
                    } else if (c == '\n') {
                        contError++;
                    //    listaErrores.add(new Error(contError, "Cadena no valida: " + lexema, linea, "Lexicos"));
                        lexema = "";
                        estado = 404;
                        i--;
                    } else {
                        lexema = lexema + c;
                        estado = 5;
                    }
                    break;
                case 6: // <= >=
                    if (c == '=' || c == '>') {
                        lexema = lexema + c;
                        addLexema(lexema, linea, "OPERADOR RELACIONAL", ">", "");
                         Analisis(">=");

                    } else {
                        addLexema(lexema, linea, "OPERADOR RELACIONAL", "<", "");
                         Analisis("<=");
                        i--;
                    }
                    lexema = "";
                    estado = 0;
                    break;
                case 404: //error
                    if (c == ' ' || c == ';' || c == '/' || c == '\t' || c == '\n') {
                        estado = 0;
                        lexema = "";
                        i--;
                    } else {
                        estado = 404;
                    }
                    break;

            }
        }
       Analisis("$");


    }
    public boolean BuscarDatoDeclarado(String lexema)
    {
    	for (Lexema item : listaLexemas) {
            if(item.getLexema().equals(lexema))
            {
           	 datofinal = item.getTD();
           	 if(!datofinal.equals(""))
           	 {
           		 return true;
           	 }
            }
         }
    	return false;
    }
    public void MostrarGeneracion()
    {
    	System.out.println(texto);
    	System.out.println("Generación de Código intermedio: ");
    	for(int i=0; i<listaGen.size(); i++)
    		System.out.println(listaGen.get(i));
    }

    /* metodo que agrega el lexema a la lista */
    public void addLexema(String cadena, int linea, String tipo, String comp, String td) {
        listaLexemas.add(new Lexema(contLexema, cadena, linea, tipo, comp, td));
        contLexema++;
    }
  
    public static void main(String[] args) {
        Semantico x= new Semantico("int a, c; \r\n"
        		+ "float b, d;\r\n"
        		+ "float var1;\r\n"
        		+ "var1=(a+b)-c/d\r\n");
        x.analizar();
       // x.MostrarGeneracion();
       // x.MostrarErrores();
    }
}


