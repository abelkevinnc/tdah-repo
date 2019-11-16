package com.tdah.util;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Items {
	private int numeroItem;
	private String descripcionItem;
	
	
	public List<Items> listaItems() {
		List<Items> list = new ArrayList<>();
		
		list.add(Items.builder()
				.numeroItem(1)
				.descripcionItem("Con frecuencia falla en PRESTAR la debida atención a los detalles o por descuido "
						+ "se cometen errores en las tareas escolares, en el trabajo o durante otras actividades (por ejemplo, se pasan por alto o se pierden detalles).")
				.build());
		list.add(Items.builder()
				.numeroItem(2)
				.descripcionItem("Con frecuencia tiene dificultades para mantener la atención en tareas o actividades recreativas (por ejemplo, tiene dificultad para mantener la atención en clases, conversaciones o lectura prolongada).")
				.build());
		list.add(Items.builder()
				.numeroItem(3)
				.descripcionItem("Con frecuencia parece no escuchar cuando se le habla directamente (por ejemplo, parece tener la mente en otras cosas, incluso en ausencia de cualquier distracción aparente).")
				.build());
		list.add(Items.builder()
				.numeroItem(4)
				.descripcionItem("Con frecuencia no sigue las INSTRUCCIONES y no termina las tareas escolares, los quehaceres o los deberes laborales (por ejemplo, inicia tareas, pero se distrae rápidamente y se evade con facilidad).")
				.build());
		
		
		list.add(Items.builder()
				.numeroItem(5)
				.descripcionItem("Con frecuencia tiene dificultad para organizar tareas y actividades (por ejemplo, dificultad para gestionar tareas secuenciales; dificultad para poner los materiales y pertenencias en orden; descuido; mala gestión del tiempo; no cumple los plazos).")
				
				.build());
		
		list.add(Items.builder()
				.numeroItem(6)
				.descripcionItem("Con frecuencia evita, le disgusta o se muestra poco entusiasta en INICIAR tareas que requieren un esfuerzo mental sostenido (por ejemplo, tareas escolares o quehaceres domésticos).")
				.build());
		list.add(Items.builder()
				.numeroItem(7)
				.descripcionItem("Con frecuencia pierde cosas necesarias para tareas o actividades (por ejemplo, materiales escolares, lápices, libros, instrumentos, billetera).")
				.build());
		list.add(Items.builder()
				.numeroItem(8)
				.descripcionItem("Con frecuencia se distrae con facilidad por estímulos externos .")
				.build());
		list.add(Items.builder()
				.numeroItem(9)
				.descripcionItem("Con frecuencia olvida las actividades cotidianas (por ejemplo, hacer las tareas, hacer las diligencias).")
				.build());
		list.add(Items.builder()
				.numeroItem(10)
				.descripcionItem("Con frecuencia juguetea o golpea con las manos o los pies o se retuerce en el asiento.")
				.build());
		list.add(Items.builder()
				.numeroItem(11)
				.descripcionItem("Con frecuencia se levanta en situaciones en que se espera que permanezca sentado.")
				.build());
		list.add(Items.builder()
				.numeroItem(12)
				.descripcionItem("Con frecuencia corretea o trepa en situaciones en las que no resulta apropiado.")
				.build());
		list.add(Items.builder()
				.numeroItem(13)
				.descripcionItem("Con frecuencia es incapaz de jugar o de ocuparse tranquilamente en ACTIVIDADES recreativas.")
				.build());
		list.add(Items.builder()
				.numeroItem(14)
				.descripcionItem("Con frecuencia está ocupado, actuando como si lo impulsara un motor (por ejemplo, es incapaz de estar o se siente incómodo estando quieto durante un tiempo prolongado).")
				.build());
		list.add(Items.builder()
				.numeroItem(15)
				.descripcionItem("Con frecuencia habla excesivamente.")
				.build());
		list.add(Items.builder()
				.numeroItem(16)
				.descripcionItem("Con frecuencia responde inesperadamente o antes de que se haya concluido una pregunta (por ejemplo, termina las frases de otros; no respeta el turno de conversación).")
				.build());
		list.add(Items.builder()
				.numeroItem(17)
				.descripcionItem("Con frecuencia le es difícil esperar su turno (por ejemplo, mientras espera una cola).")
				.build());
		list.add(Items.builder()
				.numeroItem(18)
				.descripcionItem("Con frecuencia interrumpe o se inmiscuye con otros (por ejemplo, se mete en las conversaciones, juegos o actividades; puede empezar a utilizar las cosas de otras personas sin esperar o recibir permiso).")
				.build());
		
		
		return list;
	}
	
}
