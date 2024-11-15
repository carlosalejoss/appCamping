# M12_camping
<b>Autores: Carlos Alejos (872342) y Diego Valenzuela (874085)</b>  
<b>Asignatura:</b> Ingeniería del Software, Universidad de Zaragoza

Se desea construir una aplicación que debe funcionar en dispositivos móviles con sistemas operativos Android para que el propietario o propietaria de un camping pueda gestionar las reservas que le comunican informalmente sus clientes. La aplicación exigida para este trabajo deberá permitir lo siguiente: 
•	La creación de parcelas. El camping se divide en parcelas donde los clientes del camping pueden llevar su tienda de campaña, caravana, camper o autocaravana. Cada parcela se identifica con el nombre de un monte o valle cercanos a la zona donde se encuentra el camping. Además, por cada parcela se facilitará una descripción en texto libre de las características de la parcela (tamaño, disponibilidad de agua y luz, etc.), un número máximo de ocupantes y el precio en euros por persona. 
•	La consulta de un listado de las parcelas previamente creadas, pudiéndolas ordenar por identificador, número máximo de ocupantes o precio. 
•	La modificación de las parcelas previamente creadas. 
•	La eliminación de las parcelas previamente creadas. 
•	La creación de una reserva. La reserva constará de un nombre de cliente, el número móvil del cliente, la fecha de entrada y salida, y una selección de parcelas reservadas, incluyendo el número de ocupantes en cada parcela. 
•	La comprobación de que la reserva es válida, es decir, que la fecha de entrada es igual o posterior a la del día actual, que la fecha de salida es posterior a la del día de entrada, que no se producen solapes en la reserva de parcelas y que no se supera la capacidad máxima de ocupantes en cada parcela. 
•	La consulta de un listado de las reservas previamente creadas, pudiéndolas ordenar por nombre de cliente, número de móvil, o fecha de entrada. 
•	La modificación de las reservas previamente creadas. 
•	La eliminación de las reservas previamente creadas. 
•	El cálculo del precio total de una reserva. El precio total se debería calcular de forma automática en la creación de la reserva, o en la modificación de la reserva si se realiza una selección distinta de parcelas o número de ocupantes. Por respeto a los clientes, el precio total se debería mantener aunque se modifique posteriormente el precio por persona de la parcela.
•	El envío al móvil del cliente de la información de la reserva (incluido precio) cuando el propietario o propietaria lo considere oportuno.
