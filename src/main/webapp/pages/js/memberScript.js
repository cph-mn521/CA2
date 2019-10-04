/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function fetchData(path){   
 return fetch("/CA1/api/groupmembers/"+path)
  .then(res => {return res.json();}); 
}

function buildTable(){
    var table = document.createElement("TABLE");
    table.createTHead();      
    var myTableDiv = document.getElementById("memberTable");
    
    fetch("/CA1/api/groupmembers/all").then(res=>{res.json();})
            .then(res =>{
                var row = table.insertrow(-1);
                for (var i = 0; i < res.length; i++){
                        
                }
            
            });
            
    var row = table.insertRow(-1);
    for (var i = 0; i < columnCount; i++) {
        var headerCell = document.createElement("TH");
        headerCell.innerHTML = customers[0][i];
        row.appendChild(headerCell);
    } 
   
    
    var table = document.createElement('TABLE');
    table.border = '1';

    var tableBody = document.createElement('TBODY');
    table.appendChild(tableBody);

    for (var i = 0; i < 3; i++) {
      var tr = document.createElement('TR');
      tableBody.appendChild(tr);

      for (var j = 0; j < 4; j++) {
        var td = document.createElement('TD');
        td.width = '75';
        td.appendChild(document.createTextNode("Cell " + i + "," + j));
        tr.appendChild(td);
      }
    }
    myTableDiv.appendChild(table);
  }
  
buildTable();

