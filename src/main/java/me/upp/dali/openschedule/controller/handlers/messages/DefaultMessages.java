package me.upp.dali.openschedule.controller.handlers.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultMessages {
    INFORMATION(
            """
                    \uD83D\uDD34 *GIMNASIO OPEN* \uD83D\uDD34

                    \uD83D\uDD50 Horario: 6:00 A.M. - 9:30 P.M. | Lunes - Sabados
                    *que te gustaría hacer?*\s

                    1️⃣ Cuantas personas están en el gimnasio
                    2️⃣ Solicitar acceso al gimnasio
                    3️⃣ Conocer tiempo en el gimnasio

                    _Solo escriba el numero de lo que desea hacer_"""
    ),
    CLIENTS_AMOUNT(
            "*Actualmente hay _%clientes%_ personas en el gimnasio* \uD83D\uDCAA"
    ),
    CLIENTS_REGISTER_NAME(
            """
                    \uD83D\uDD10 Solicitud de acceso \uD83D\uDD10

                    \uD83D\uDC65 Por favor escribe tu nombre:

                    \uD83D\uDEAB Para cancelar escribe: cancelar"""
    ),
    CLIENTS_REGISTER_KNOWN_CLIENT(
            """
                    \uD83D\uDD10 *Solicitud de acceso* \uD83D\uDD10

                    \uD83D\uDC4B Hola, eres *%cliente%*.

                    _Por favor responde con *SI* o *NO*_

                    \uD83D\uDEAB Para cancelar escribe: *cancelar*"""
    ),
    CLIENTS_CODE(
            """
                    \uD83D\uDD10 *Solicitud de acceso* \uD83D\uDD10

                    \uD83D\uDC64%cliente% tu código es: *%codigo%*

                    _*Tienes %tiempo% para que caduque el código*_

                    \uD83D\uDEAB Para cancelar el acceso escribe: *cancelar %codigo%*"""
    ),
    CLIENTS_CODE_EXPIRED(
            """
                    \uD83D\uDD10 *Solicitud de acceso* \uD83D\uDD10

                    \uD83D\uDC64%cliente% tu código *%codigo%* caducó.

                    *Si aun quieres ingresar al gimnasio por favor solicita uno nuevo.*"""
    ),
    CLIENTS_TIME_FINISHED(
            """
                    \uD83D\uDD34 *GIMNASIO OPEN* \uD83D\uDD34

                    \uD83D\uDC64%cliente% tu tiempo se ha terminado!

                    _Gracias por tu preferencia_\s"""
    ),
    CLIENTS_TIME(
            """
                    ⏱️ *Tiempo* ⏱️

                    \uD83D\uDC64%cliente% iniciaste a las %tiempo-inicio% y termina a las %tiempo-fin%."""
    );

    private final String message;
}
