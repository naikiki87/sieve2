<template>
  <div style="position : absolute; top : 10px; width : 1000px; background:white;">
    <div id="content">
      <svg id="arrowbg"> </svg>
    </div>
    <div id="header">
      <table valign="middle" align="left" style="height:70px;margin:auto; width:250px; padding-top : 5px; background:;">
        <tr>
          <td v-model="jobId" style="font-size:22px; font-weight:700;"> job : #{{ jobId }} </td>
          <td v-model="localcnt" style="font-size:14px;"> 연산자 수 : {{ localcnt }} </td>
        </tr>
      </table>

      <table id="TDHeader_table" style=" margin : auto; width:89%;">
        <tr>
          <td colspan="2" style="background:gold;" v-model="mode"> {{ mode }} </td>
        </tr>
      </table>

      <table id="TDHeader_table" style=" margin : auto; width:97%;">
        <tr>
          <td colspan="2"> <button id="modeChanger" class="JobDetailHeaderbutton" v-on:click="changeMode"> STATUS CHANGE </button> </td>
        </tr>
      </table>

      <table id="TDHeader_table" style=" margin : auto; width:97%;">
        <tr>
          <td colspan="2"> <button style="background:lightblue;" class="JobDetailHeaderbutton" v-on:click="boxPositionSave"> TASK SHAPE 저장 </button> </td>
        </tr>
        <br>
        <tr>
          <td> <button class="JobDetailHeaderbutton2" v-on:click="jobDistTotal"> TASK 배포 </button> </td>
          <td> <button class="JobDetailHeaderbutton2" v-on:click="jobRUN"> JOB 실행 </button> </td>
        </tr>
      </table>
    </div>
    <div id="rightaside">
      <table class="TDrightTable" style="height:30px; width:100%; border:0px; color:white;">
        <tr>
          <td style="background:black; font-size:14px; font-weight:700;"> ---- 연산자 추가 ---- </td>
        </tr>
      </table>
      <br>
      <table class="TDrightTable">
        <tr>
          <td style="width : 80px;" class="add_title"> 연산자 </td>
          <td>
            <select style="width : 99%;" v-model="task_id">
              <option v-for="(item, index) in taskListArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
            </select>
          </td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 수행 서버 </td>
          <td>
            <select style="width : 99%;" v-model="ec_id">
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{ item.ip_address }} </option>
            </select>
          </td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 리스닝 포트 </td>
          <td> <input style="width : 95%;" type="text" v-model="listening_port"></td>
        </tr>
      </table>
      <br>
      <br>
      <button style="height:40px; width:200px; font-size:14px;" class="JobDetailHeaderbutton" v-on:click="addTask_reload"> 연산자 추가 </button>
      <button style="height:40px; width:200px; font-size:14px;" class="JobDetailHeaderbutton" v-on:click="func_test"> ㅅㄷㄴㅅ </button>
    </div>
    <div id="context-menus" class="context-menus">
      <p class="cntxtmenuItem" v-on:click="openTaskModify"> Task 수정 </p>
      <p class="cntxtmenuItem" v-on:click="deleteTask_reload"> Task 삭제 </p>
    </div>

    <div id="modify_BS">
      <div class="modify_window_title">
        <h2 v-model="taskID"> Task ID : {{ taskID }} </h2>
      </div>
      <br>
      <table style="margin:auto; align:center;">
        <tr>
          <td style="width : 80px;" class="add_title"> 연산자 </td>
          <td>
            <h3 v-model="temptask_name" class="modify_window_op_title"> {{ temptask_name }} </h3>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 수행 서버 </td>
          <td>
            <select style="width : 99%;" v-model="tempec_id">
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 수행 포트 </td>
          <td> <input style="width : 95%;" type="text" v-model="templistening_port"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적 IP </td>
          <td>
            <select style="width : 99%;" v-model="temp_para[0]">
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
            </select>
          </td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적 Port </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[1]"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 전송 속도 </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[2]"></td>
        </tr>
      </table>
      <br> <br>
      <button id="modifyButton" v-on:click="modifyTask_reload"> SAVE </button>
      <button id="modifyButton" v-on:click="hideModifyMenu"> CLOSE </button>
    </div>
    <div id="modify_SO">
      <div class="modify_window_title">
        <h2 v-model="taskID"> Task ID : {{ taskID }} </h2>
      </div>
      <br>
      <table style="margin:auto; align:center;">
        <tr>
          <td style="width : 80px;" class="add_title"> 연산자 </td>
          <td>
            <h3 v-model="temptask_name" class="modify_window_op_title"> {{ temptask_name }} </h3>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 수행 서버 </td>
          <td>
            <select style="width : 99%;" v-model="tempec_id">
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 수행 포트 </td>
          <td> <input style="width : 95%;" type="text" v-model="templistening_port"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적 IP </td>
          <td>
            <select style="width : 99%;" v-model="temp_para[0]">
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
            </select>
          </td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적 Port </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[1]"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> Output 방식 </td>
          <td>
            <!-- <select id="taskSelect" style="width : 99%; border:1px solid;" v-model="so_out_type"> -->
            <select id="taskSelect" style="width : 99%; border:1px solid;" v-model="temp_para[2]">
              <option value = "0"> socket </option>
              <option value = "1"> db </option>
              <option value = "2"> file </option>
            </select>
          </td>
        </tr>

        <!-- <tr v-if="so_out_type === '0'">
          <td style="background:lightgrey;"> 출력 IP </td>
          <td> <input style="width : 95%;" type="text" v-model="so_out_ip"></td>
        </tr>
        <tr v-if="so_out_type === '0'">
          <td style="background:lightgrey;"> 출력 Port </td>
          <td> <input style="width : 95%;" type="text" v-model="so_out_port"></td>
        </tr> -->

        <!-- <tr v-if="so_out_type === '1'">
          <td style="background:lightgrey;"> 출력 IP </td>
          <td> <input style="width : 95%;" type="text" v-model="so_out_ip"></td>
        </tr>
        <tr v-if="so_out_type === '1'">
          <td style="background:lightgrey;"> 출력 Port </td>
          <td> <input style="width : 95%;" type="text" v-model="so_out_port"></td>
        </tr> -->
        <tr v-if="temp_para[2] === '1'">
          <td style="background:lightgrey;"> DB </td>
          <!-- <td> <input style="width : 95%;" type="text" v-model="so_type_1_db"></td> -->
          <td> <input style="width : 95%;" type="text" v-model="temp_para[3]"></td>
        </tr>
        <tr v-if="temp_para[2] === '1'">
          <td style="background:lightgrey;"> Table </td>
          <!-- <td> <input style="width : 95%;" type="text" v-model="so_type_1_table"></td> -->
          <td> <input style="width : 95%;" type="text" v-model="temp_para[4]"></td>
        </tr>

        <!-- <tr v-if="so_out_type === '2'">
          <td style="background:lightgrey;"> 출력 IP </td>
          <td> <input style="width : 95%;" type="text" v-model="so_out_ip"></td>
        </tr>
        <tr v-if="so_out_type === '2'">
          <td style="background:lightgrey;"> 출력 Port </td>
          <td> <input style="width : 95%;" type="text" v-model="so_out_port"></td>
        </tr> -->
        <tr v-if="temp_para[2] === '2'">
          <td style="background:lightgrey;"> 파일명 </td>
          <!-- <td> <input style="width : 95%;" type="text" v-model="so_type_2_filename"></td> -->
          <td> <input style="width : 95%;" type="text" v-model="temp_para[3]"></td>
        </tr>
        
      </table>
      <br> <br>
      <button id="modifyButton" v-on:click="modifyTask_reload"> SAVE </button>
      <button id="modifyButton" v-on:click="hideModifyMenu"> CLOSE </button>
    </div>
    <div id="modify_1_itemFP">
      <div class="modify_window_title">
        <h2 v-model="taskID"> Task ID : {{ taskID }} </h2>
      </div>
      <br>
      <table style="margin:auto; align:center;">
        <tr>
          <td style="width : 80px;" class="add_title"> 연산자 </td>
          <td>
            <h3 v-model="temptask_name" class="modify_window_op_title"> {{ temptask_name }} </h3>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 수행 서버 </td>
          <td>
            <select style="width : 99%;" v-model="tempec_id">
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 수행 포트 </td>
          <td> <input style="width : 95%;" type="text" v-model="templistening_port"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적 IP </td>
          <td>
            <select style="width : 99%;" v-model="temp_para[0]">
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
            </select>
          </td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적 Port </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[1]"></td>
        </tr>


        <tr>
          <td style="width : 80px;" class="add_title"> 집계 기준 </td>
          <td>
            <select id="taskSelect" style="width : 99%; border:1px solid;" v-model="temp_para[2]">
              <option value = "0"> Time </option>
              <option value = "1"> Tuple </option>
            </select>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 집계 단위 </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[3]"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> Min Support </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[4]"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> Max Support </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[5]"></td>
        </tr>
        
      </table>
      <br> <br>
      <button id="modifyButton" v-on:click="modifyTask_reload"> SAVE </button>
      <button id="modifyButton" v-on:click="hideModifyMenu"> CLOSE </button>
    </div>
    <div id="modify_n_itemFP">
      <div class="modify_window_title">
        <h2 v-model="taskID"> Task ID : {{ taskID }} </h2>
      </div>
      <br>
      <table style="margin:auto; align:center;">
        <tr>
          <td style="width : 80px;" class="add_title"> 연산자 </td>
          <td>
            <h3 v-model="temptask_name" class="modify_window_op_title"> {{ temptask_name }} </h3>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 수행 서버 </td>
          <td>
            <select style="width : 99%;" v-model="tempec_id">
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 수행 포트 </td>
          <td> <input style="width : 95%;" type="text" v-model="templistening_port"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적 IP </td>
          <td>
            <select style="width : 99%;" v-model="temp_para[0]">
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
            </select>
          </td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적 Port </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[1]"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> n item </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[2]"></td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 집계 기준 </td>
          <td>
            <select id="taskSelect" style="width : 99%; border:1px solid;" v-model="temp_para[3]">
              <option value = "0"> Time </option>
              <option value = "1"> Tuple </option>
            </select>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 집계 단위 </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[4]"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> Min Support </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[5]"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> Max Support </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[6]"></td>
        </tr>
      </table>
      <br> <br>
      <button id="modifyButton" v-on:click="modifyTask_reload"> SAVE </button>
      <button id="modifyButton" v-on:click="hideModifyMenu"> CLOSE </button>
    </div>
    <div id="modify_TR">
      <div class="modify_window_title">
        <h2 v-model="taskID"> Task ID : {{ taskID }} </h2>
      </div>
      <br>
      <table style="margin:auto; align:center;">
        <tr>
          <td style="width : 80px;" class="add_title"> 연산자 </td>
          <td>
            <h3 v-model="temptask_name" class="modify_window_op_title"> {{ temptask_name }} </h3>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 수행 서버 </td>
          <td>
            <select style="width : 99%;" v-model="tempec_id">
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 수행 포트 </td>
          <td> <input style="width : 95%;" type="text" v-model="templistening_port"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적 IP </td>
          <td>
            <select style="width : 99%;" v-model="temp_para[0]">
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
            </select>
          </td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적 Port </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[1]"></td>
        </tr>
      </table>
      <br> <br>
      <button id="modifyButton" v-on:click="modifyTask_reload"> SAVE </button>
      <button id="modifyButton" v-on:click="hideModifyMenu"> CLOSE </button>
    </div>
    <div id="modify_Proj">
      <div class="modify_window_title">
        <h2 v-model="taskID"> Task ID : {{ taskID }} </h2>
      </div>
      <br>
      <table style="margin:auto; align:center;">
        <tr>
          <td style = "width : 200px;">
            <tr>
              <td style="width : 80px;" class="add_title"> 연산자 </td>
              <td>
                <h3 v-model="temptask_name" class="modify_window_op_title"> {{ temptask_name }} </h3>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 입력 스키마 </td>
              <td>
                <select style="width : 99%;" v-model="tempinput_schema">
                  <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
                </select>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 리스닝 포트 </td>
              <td> <input style="width : 95%;" type="text" v-model="templistening_port"></td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 수행 서버 </td>
              <td>
                <select style="width : 99%;" v-model="tempec_id">
                  <option v-for="(item, index) in svrArray" :value="item.id"> {{ item.id }} </option>
                </select>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 출력 타입 </td>
              <td>
                <select id="taskSelect" style="width : 99%; border:1px solid;" v-model="tempoutput_type">
                  <option value = "0"> Socket Output </option>
                  <option value = "1"> File Output </option>
                  <option value = "2"> DB Output </option>
                </select>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> CONFIG </td>
              <td> <input style="width : 95%;" type="text" v-model="tempconfig"> </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> HB TASK </td>
              <td> <input style="width : 95%;" type="text" v-model="tempheartbeat_task"></td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> HB JOB </td>
              <td> <input style="width : 95%;" type="text" v-model="tempheartbeat_job"></td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 출력 스키마 </td>
              <td>
                <select style="width : 99%;" v-model="tempoutput_schema">
                  <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
                </select>
              </td>
            </tr>
          </td>
          <td> <div class="proj_attr_btwbox"> </div> </td>
          <td>
            <div class="proj_attr_box">
              <div> <h3 class="proj_attr_title"> 속성 </h3> </div>
              <div>
                <select class="proj_attr_list" id="projection_sel_box" size=4>
                  <option v-for="(item, index) in parentAttrs" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
                </select>
              </div>
            </div>
          </td>
          <td>
            <div class="proj_attr_btwbox">
              <button style="margin-top : 120px; width : 50px;" v-on:click="projAddAttr"> 추가 </button>
            </div>
          </td>
          <td>
            <div class="proj_attr_box">
              <div> <h3 class="proj_attr_title"> 선택 속성 </h3> </div>
              <div>
                <select class="proj_attr_list" id="projection_selected_box" size=4>
                  <option v-for="(item, index) in myAttrs" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
                </select>
              </div>
            </div>
          </td>
        </tr>
      </table>
      <br> <br>
      <button id="modifyButton" v-on:click="modifyTask_reload"> SAVE </button>
      <button id="modifyButton" v-on:click="hideModifyMenu"> CLOSE </button>
    </div>
    <div id="modify_Agg">
      <div class="modify_window_title">
        <h2 v-model="taskID"> Task ID : {{ taskID }} </h2>
      </div>
      <br>
      <table style="margin:auto; align:center;">
        <tr>
          <td style = "width : 200px;">
            <tr>
              <td style="width : 80px;" class="add_title"> 연산자 </td>
              <td>
                <h3 v-model="temptask_name" class="modify_window_op_title"> {{ temptask_name }} </h3>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 입력 스키마 </td>
              <td>
                <select style="width : 99%;" v-model="tempinput_schema">
                  <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
                </select>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 리스닝 포트 </td>
              <td> <input style="width : 95%;" type="text" v-model="templistening_port"></td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 수행 서버 </td>
              <td>
                <select style="width : 99%;" v-model="tempec_id">
                  <option v-for="(item, index) in svrArray" :value="item.id"> {{ item.id }} </option>
                </select>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 출력 타입 </td>
              <td>
                <select id="taskSelect" style="width : 99%; border:1px solid;" v-model="tempoutput_type">
                  <option value = "0"> Socket Output </option>
                  <option value = "1"> File Output </option>
                  <option value = "2"> DB Output </option>
                </select>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> CONFIG </td>
              <td> <input style="width : 95%;" type="text" v-model="tempconfig"> </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> HB TASK </td>
              <td> <input style="width : 95%;" type="text" v-model="tempheartbeat_task"></td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> HB JOB </td>
              <td> <input style="width : 95%;" type="text" v-model="tempheartbeat_job"></td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 출력 스키마 </td>
              <td>
                <select style="width : 99%;" v-model="tempoutput_schema">
                  <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
                </select>
              </td>
            </tr>
          </td>
          <td> <div class="agg_btwbox"> </div> </td>

          <td>
            <div style="width:100px; ">
              <div id="tb_1" class="tabmenu active" v-on:click="tabChange('tb_1', 'AggGeneral')"> GENERAL </div>
              <div id="tb_2" class="tabmenu" v-on:click="tabChange('tb_2', 'AggGroupBy')"> GROUP BY </div>
              <div id="tb_3" class="tabmenu" v-on:click="tabChange('tb_3', 'AggHaving')"> HAVING </div>
              <div id="tb_4" class="tabmenu" v-on:click="tabChange('tb_4', 'AggSelect')"> SELECT </div>
              <div id="tb_5" class="tabmenu" v-on:click="tabChange('tb_5', 'AggOrderBy')"> ORDER BY </div>
            </div>
          </td>

          <td>
            <div id="AggGeneral" class="aggtabcontent" >
              <div id="AggConfig_title"> GENERAL </div>
              <table id="AggConfig_table" style=" margin : auto; width:97%;">
              <colgroup>
                <col style="width: 120px"> <!-- Name -->
                <col style="width: 80px"> <!-- value -->
                <col style="width: 250px"> <!-- description -->
              </colgroup>

              <th>Name</th>
              <th>Value</th>
              <th>Description</th>

              <tr>
                <td> Input Queue Size </td>
                <td> <input class="AggGeneralInput" type="text" v-model="val_InputQueueSize"> </td>
                <td> <input style="width : 95%;" type="text" v-model="des_InputQueueSize"> </td>
              </tr>

              <tr>
                <td> Window Type </td>
                <td>
                  <select id="AggWindowType" v-model="val_WindowType">
                    <option value = "0"> TUPLE </option>
                    <option value = "1"> TIME </option>
                  </select>
                </td>
                <td> <input style="width : 95%;" type="text" v-model="des_WindowType"> </td>
              </tr>

              <tr>
                <td> Window Size </td>
                <td> <input class="AggGeneralInput" type="text" v-model="val_WindowSize"> </td>
                <td> <input style="width : 95%;" type="text" v-model="des_WindowSize"> </td>
              </tr>

              <tr>
                <td> Number of Pane </td>
                <td> <input class="AggGeneralInput" type="text" v-model="val_NumberofPane"> </td>
                <td> <input style="width : 95%;" type="text" v-model="des_NumberofPane"> </td>
              </tr>

              <tr>
                <td> Sliding Size </td>
                <td> <input class="AggGeneralInput" type="text" v-model="val_SlidingSize"> </td>
                <td> <input style="width : 95%;" type="text" v-model="des_SlidingSize"> </td>
              </tr>


            </table>
            </div>
            <div id="AggGroupBy" class="aggtabcontent" style="display:none;" >
              <AggGroupBy v-bind:configData="AggConfigTrans" v-bind:taskID="taskID" v-bind:parentAttrs="parentAttrs" @query-add="aggQueryAddGB"> </AggGroupBy>
            </div>
            <div id="AggHaving" class="aggtabcontent" style="display:none;">
              <AggHaving v-bind:configData="tempconfig" v-bind:taskID="taskID" v-bind:parentAttrs="parentAttrs" @query-add="aggQueryAddHAV"> </AggHaving>
            </div>
            <div id="AggSelect" class="aggtabcontent" style="display:none;">
              <AggSelect v-bind:configData="tempconfig" v-bind:taskID="taskID" v-bind:parentAttrs="parentAttrs" @query-add="aggQueryAddSelect" @obItem-add="makeOBItemList"> </AggSelect>
            </div>
            <div id="AggOrderBy" class="aggtabcontent" style="display:none;">
              <AggOrderBy v-bind:configData="tempconfig" v-bind:aggOBItems="aggOBItems" @query-add="aggQueryAddOB"> </AggOrderBy>
            </div>
          </td>
        </tr>
        <tr>
          <td> </td>
          <td> </td>
          <td style="font-size:15px; font-weight:700;"> QUERY : </td>
          <td>
            <input style="width : 99%; height:100px;" type="textarea" v-model="AggQuery">
          </td>
        </tr>
      </table>
      <br>
      <table>
      </table>
      <button id="modifyButton" v-on:click="modifyTask_reload"> SAVE </button>
      <button id="modifyButton" v-on:click="makeAggTOTALQueryData"> TEST </button>
      <button id="modifyButton" v-on:click="hideModifyMenu"> CLOSE </button>
    </div>
    <div id="modify_HJ">
      <div class="modify_window_title">
        <h2 v-model="taskID"> Task ID : {{ taskID }} </h2>
      </div>
      <br>
      <table style="margin:auto; align:center;">
        <tr>
          <td style = "width : 200px;">
            <tr>
              <td style="width : 80px;" class="add_title"> 연산자 </td>
              <td>
                <h3 v-model="temptask_name" class="modify_window_op_title"> {{ temptask_name }} </h3>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 입력 스키마 </td>
              <td>
                <select style="width : 99%;" v-model="tempinput_schema">
                  <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
                </select>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 리스닝 포트 </td>
              <td> <input style="width : 95%;" type="text" v-model="templistening_port"></td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 수행 서버 </td>
              <td>
                <select style="width : 99%;" v-model="tempec_id">
                  <option v-for="(item, index) in svrArray" :value="item.id"> {{ item.id }} </option>
                </select>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 출력 타입 </td>
              <td>
                <select id="taskSelect" style="width : 99%; border:1px solid;" v-model="tempoutput_type">
                  <option value = "0"> Socket Output </option>
                  <option value = "1"> File Output </option>
                  <option value = "2"> DB Output </option>
                </select>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> CONFIG </td>
              <td> <input style="width : 95%;" type="text" v-model="tempconfig"> </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> HB TASK </td>
              <td> <input style="width : 95%;" type="text" v-model="tempheartbeat_task"></td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> HB JOB </td>
              <td> <input style="width : 95%;" type="text" v-model="tempheartbeat_job"></td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 출력 스키마 </td>
              <td>
                <select style="width : 99%;" v-model="tempoutput_schema">
                  <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
                </select>
              </td>
            </tr>
          </td>
          <td> <div class="agg_btwbox"> </div> </td>
          <td rowspan="2">
            <div id="frameHalfJoin">
              <!-- <AggGroupBy v-bind:taskID="taskID" v-bind:parentAttrs="parentAttrs" @query-add="aggQueryAddGB"> </AggGroupBy> -->
              <HalfJoin v-bind:parent="parent" v-bind:HJfromID="HJfromID" v-bind:parentAttrs="parentAttrs" v-bind:taskID="taskID" @HJqueryAdd="HJqueryAdd"> </HalfJoin>
            </div>
          </td>
        </tr>
        <tr>
          <td> <button style="height:170px; width : 150px;" v-on:click="abcdparent"> TEST </button> </td>
        </tr>
      </table>
      <br>
      <table>
      </table>
      <button id="modifyButton" v-on:click="modifyTask_reload"> SAVE </button>
      <button id="modifyButton" v-on:click="hideModifyMenu"> CLOSE </button>
    </div>
    <div id="modify_BJ">
      <div class="modify_window_title">
        <h2 v-model="taskID"> Task ID : {{ taskID }} </h2>
      </div>
      <br>
      <table style="margin:auto; align:center;">
        <tr>
          <td style = "width : 200px;">
            <tr>
              <td style="width : 80px;" class="add_title"> 연산자 </td>
              <td>
                <h3 v-model="temptask_name" class="modify_window_op_title"> {{ temptask_name }} </h3>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 입력 스키마 </td>
              <td>
                <select style="width : 99%;" v-model="tempinput_schema">
                  <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
                </select>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 리스닝 포트 </td>
              <td> <input style="width : 95%;" type="text" v-model="templistening_port"></td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 수행 서버 </td>
              <td>
                <select style="width : 99%;" v-model="tempec_id">
                  <option v-for="(item, index) in svrArray" :value="item.id"> {{ item.id }} </option>
                </select>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 출력 타입 </td>
              <td>
                <select id="taskSelect" style="width : 99%; border:1px solid;" v-model="tempoutput_type">
                  <option value = "0"> Socket Output </option>
                  <option value = "1"> File Output </option>
                  <option value = "2"> DB Output </option>
                </select>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> CONFIG </td>
              <td> <input style="width : 95%;" type="text" v-model="tempconfig"> </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> HB TASK </td>
              <td> <input style="width : 95%;" type="text" v-model="tempheartbeat_task"></td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> HB JOB </td>
              <td> <input style="width : 95%;" type="text" v-model="tempheartbeat_job"></td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 출력 스키마 </td>
              <td>
                <select style="width : 99%;" v-model="tempoutput_schema">
                  <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
                </select>
              </td>
            </tr>
          </td>
          <td> <div class="agg_btwbox"> </div> </td>
          <td rowspan="2">
            <div id="frameBinJoin">
              <BinaryJoin v-bind:BJfromID="HJfromID" v-bind:schemaAttrs="parentAttrs" v-bind:taskID="taskID" @BJqueryAdd="BJqueryAdd"> </BinaryJoin>
            </div>
          </td>
        </tr>
        <tr>
          <td> <button style="height:170px; width : 150px;" v-on:click="abcdparent"> TEST </button> </td>
        </tr>
      </table>
      <br>
      <table>
      </table>
      <button id="modifyButton" v-on:click="modifyTask_reload"> SAVE </button>
      <button id="modifyButton" v-on:click="hideModifyMenu"> CLOSE </button>
    </div>
    <!-- <div id="modify_TR">
      <div class="modify_window_title">
        <h2 v-model="taskID"> Task ID : {{ taskID }} </h2>
      </div>
      <br>
      <table style="margin:auto; align:center;">
        <tr>
          <td style = "width : 200px;">
            <tr>
              <td style="width : 80px;" class="add_title"> 연산자 </td>
              <td>
                <h3 v-model="temptask_name" class="modify_window_op_title"> {{ temptask_name }} </h3>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 입력 스키마 </td>
              <td>
                <select style="width : 99%;" v-model="tempinput_schema">
                  <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
                </select>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 리스닝 포트 </td>
              <td> <input style="width : 95%;" type="text" v-model="templistening_port"></td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 수행 서버 </td>
              <td>
                <select style="width : 99%;" v-model="tempec_id">
                  <option v-for="(item, index) in svrArray" :value="item.id"> {{ item.id }} </option>
                </select>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 출력 타입 </td>
              <td>
                <select id="taskSelect" style="width : 99%; border:1px solid;" v-model="tempoutput_type">
                  <option value = "0"> Socket Output </option>
                  <option value = "1"> File Output </option>
                  <option value = "2"> DB Output </option>
                </select>
              </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> CONFIG </td>
              <td> <input style="width : 95%;" type="text" v-model="tempconfig"> </td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> HB TASK </td>
              <td> <input style="width : 95%;" type="text" v-model="tempheartbeat_task"></td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> HB JOB </td>
              <td> <input style="width : 95%;" type="text" v-model="tempheartbeat_job"></td>
            </tr>
            <tr>
              <td style="width : 80px;" class="add_title"> 출력 스키마 </td>
              <td>
                <select style="width : 99%;" v-model="tempoutput_schema">
                  <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
                </select>
              </td>
            </tr>
          </td>
          <td rowspan="2">
            <div id="frameTransaction">
              <Transaction v-bind:schemaAttrs="parentAttrs" v-bind:taskID="taskID" @TRqueryAdd="TRqueryAdd"> </Transaction>
            </div>
          </td>
        </tr>
      </table>
      <br>
      <button id="modifyButton" v-on:click="modifyTask_reload"> SAVE </button>
      <button id="modifyButton" v-on:click="hideModifyMenu"> CLOSE </button>
    </div> -->

  </div>
</template>

<script>
import axios from 'axios'
import serverConfig from "../assets/data/server_config.json";
import AggGeneral from './AggGeneral.vue'
import AggGroupBy from './AggGroupBy.vue'
import AggHaving from './AggHaving.vue'
import AggSelect from './AggSelect.vue'
import AggOrderBy from './AggOrderBy.vue'
import HalfJoin from '../components/configWindow/HalfJoin'
import BinaryJoin from '../components/configWindow/BinaryJoin'
import Transaction from '../components/configWindow/Transaction'

var serverADDR = serverConfig.hostserver;
var jobID = getParameterByName('jobID');

var drag_flag=0;
var src;
var dest;
var startid, destid;
var x1, y1, x2, y2;
var divE1, divE2;
var divwid1, divhei1, divwid2, divhei2;
var div_cnt = 0;
var marginX=0;
var marginY=0;
var cntxtmenu;
var modify_menu;
var modify_BS, modify_Proj, modify_SO, modify_Agg, modify_HJ, modify_BJ, modify_TR, modify_1_itemFP, modify_n_itemFP;
var cntxtmenuID;
var t;
var taskArray = [];
var lineArray = [];
var call = 0;

function returnArray(index) {
  var output, arr, returnArr;
  localStorage.removeItem("loglevel:webpack-dev-server");

  for(var i=0; i<localStorage.length; i++) {
    output = localStorage.getItem(i);
    arr = JSON.parse(output);

    if(arr[0] == index) {
      returnArr = arr;
    }
  }

  return returnArr;
}
function addLine_reload(start, end) {
  new Promise(function(resolve, reject){
    addLine(start, end);
    setTimeout(function() {
      resolve(1);
    }, 200);
  })
  .then(function(result) {
    clearLine();
    return;
  })
  .then(function(result) {
    loadLine(jobID);
    return;
  });
}
function loadLine(index) {
  var params = { job_id: index }
  var api = "http://" + serverADDR + ":3000/users/task_lines";

  axios.post(api, params)
  .then( response => {
    lineArray = response.data;
    createLine();
  })
  .catch( response => { console.log(response) } );
}
function getParameterByName(name) {
  name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
  var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
  results = regex.exec(location.search);
  return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}
function clearLine() {
  console.log("func clear LINE");
  var select = 0;
  var arrowbg = document.getElementById("arrowbg");
  var arrowbgNodeLength = arrowbg.childNodes.length;

  while(arrowbgNodeLength > 0) {
    arrowbg.removeChild(arrowbg.firstChild);
    arrowbgNodeLength = arrowbgNodeLength - 1;
  }
}
function addLine(start, end) {
  console.log("ADD LINE");
  console.log("job_id : " + jobID);
  console.log("start : " + start);
  console.log("end : " + end);

  var params = {
    job_id : jobID,
    from_id : start,
    to_id : end
  }

  var api = "http://" + serverADDR + ":3000/users/task_lines/add";
  axios.post(api, params)
  .then(response => {})
  .catch(err => {});
}
function createLine() {
  var output, arr;
  var start_x, start_y, end_x, end_y;
  var id;

  for(var i=0; i<lineArray.length; i++) {
    id = lineArray[i].id;

    if(call == 0) {
      start_x = get_position_x(lineArray[i].from_id);
      start_y = get_position_y(lineArray[i].from_id);
      end_x = get_position_x(lineArray[i].to_id);
      end_y = get_position_y(lineArray[i].to_id);
    }
    else {
      start_x = get_position_x(lineArray[i].from_id) - marginX;
      start_y = get_position_y(lineArray[i].from_id) - marginY;
      end_x = get_position_x(lineArray[i].to_id) + marginX;
      end_y = get_position_y(lineArray[i].to_id) + marginY;
    }

    // console.log(" ---- " + i + " ---- ");
    // console.log("staX : " + start_x);
    // console.log("staY : " + start_y);
    // console.log("endX : " + start_x);
    // console.log("endY : " + start_y);

    drawLine(start_x, start_y, end_x, end_y, 90, 90 ,90, 90, id);
  }
  call = 1;
}
function deleteLine(index) {
  console.log("DEL LINE");

  var params = {
    id : index
  }

  var api = "http://" + serverADDR + ":3000/users/task_lines/delete"
  axios.post(api, params)
  .then(response => { })
  .catch(err => { });
}
function get_position_x(index) {
  var output, arr;
  var localkey;
  var position;

  localStorage.removeItem("loglevel:webpack-dev-server");

  for(var i=0; i<localStorage.length; i++) {
    output = localStorage.getItem(i);
    arr = JSON.parse(output);
    if(arr[0] == index) {
      position = arr[11];
    }
  }
  return position;
}
function get_position_y(index) {
  var output, arr;
  var localkey;
  var position;

  localStorage.removeItem("loglevel:webpack-dev-server");

  for(var i=0; i<localStorage.length; i++) {
    output = localStorage.getItem(i);
    arr = JSON.parse(output);
    if(arr[0] == index) {
      position = arr[12];
    }
  }
  return position;
}
function drawLine(start_x, start_y, dest_x, dest_y, w1, h1, w2, h2, lineID) {  // drawing line
  //console.log("Draw Line222");
  var startpt_x = 0, startpt_y=0, destpt_x=0, destpt_y=0;
  var box_margin = 2;
  var s_top = start_y - box_margin;
  var d_top = dest_y - box_margin;
  var s_bottom = start_y + h1 + box_margin;
  var d_bottom = dest_y + h2 + box_margin;
  var s_left = start_x - box_margin;
  var d_left = dest_x - box_margin;
  var s_right = start_x + w1 + box_margin;
  var d_right = dest_x + w2 + box_margin;

  var r__x, r__y, l__x, l__y;
  var m__x, m__y;
  var degree, v_degree;
  var _size;
  var c;
  var step = 1;
  var _dif;
  var _len;

  if(s_top >= d_bottom) {
    if(s_left >= (d_right + w2)) {
      ////console.log("case 1");
      startpt_x = s_left;
      startpt_y = s_top + h1/2;
      destpt_x = d_right;
      destpt_y = d_top + h2/2;
    }
    else if((s_right+w1) <= d_left) {
      ////console.log("case 2");
      startpt_x = s_right;
      startpt_y = s_top + h1/2;
      destpt_x = d_left;
      destpt_y = d_top + h2/2;
    }
    else {
      ////console.log("case 3");
      startpt_x = s_left + w1/2;
      startpt_y = s_top;
      destpt_x = d_left + w2/2;
      destpt_y = d_bottom;
    }
  }
  else if(s_bottom <= d_top) {
    if(s_left >= (d_right+w2)) {
      ////console.log("case 4");
      startpt_x = s_left;
      startpt_y = s_top + h1/2;
      destpt_x = d_right;
      destpt_y = d_top + h2/2;
    }
    else if((s_right+w1) <= d_left) {
      ////console.log("case 5");
      startpt_x = s_right;
      startpt_y = s_top + h1/2;
      destpt_x = d_left;
      destpt_y = d_top + h2/2;
    }
    else {
      ////console.log("case 6");
      startpt_x = s_left + w1/2;
      startpt_y = s_bottom;
      destpt_x = d_left + w2/2;
      destpt_y = d_top;
    }
  }
  else{
    if(s_left >= d_right) {
      ////console.log("case 7");
      startpt_x = s_left;
      startpt_y = s_top + h1/2;
      destpt_x = d_right;
      destpt_y = d_top + h2/2;
    }
    else if(s_right <= d_left) {
      ////console.log("case 8");
      startpt_x = s_right;
      startpt_y = s_top + h1/2;
      destpt_x = d_left;
      destpt_y = d_top + h2/2;
    }
  }

  degree = (destpt_y - startpt_y) / (destpt_x - startpt_x);

  if(degree == "Infinity") { degree = 1000; }
  if(degree == "-Infinity") { degree = -1000; }
  if(degree == 0) { degree = 0.001; }
  v_degree = -1 / degree;
  _len = 5;

  if(destpt_x >= startpt_x) { c = 1; }
  else{ c = 2; }

  m__x = arrow_x(destpt_x, destpt_y, degree, _len, c, step);
  m__y = arrow_y(m__x, destpt_x, destpt_y, degree);

  step = 1;
  r__x = arrow_x(m__x, m__y, v_degree, _len, c, step);
  r__y = arrow_y(r__x, m__x, m__y, v_degree);

  step = 2;
  l__x = arrow_x(m__x, m__y, v_degree, _len, c, step);
  l__y = arrow_y(l__x, m__x, m__y, v_degree);


  var svgdiv = document.getElementById('arrowbg');
  var path = document.createElementNS('http://www.w3.org/2000/svg', 'path');

  // path.setAttribute('id', 'linkid');
  // console.log("line id : " + lineID);

  path.setAttribute('id', lineID);
  path.setAttribute('class', 'linkline');
  path.setAttribute('d', "M" + startpt_x + "," + startpt_y + "," + destpt_x + "," + destpt_y + "," + r__x + "," + r__y + "," + l__x + "," + l__y + "," + destpt_x + "," + destpt_y);
  //path.setAttribute('d', "M" + startpt_x + "," + startpt_y + " " + destpt_x + "," + destpt_y + " " + r__x + "," + r__y + " " + l__x + "," + l__y + " " + destpt_x + "," + destpt_y);
  svgdiv.appendChild(path);
}
function arrow_x(a, b, d, l, c, step) {  // 화살표 오른쪽 좌표
  var x1, x2, result;
  var A = 1 + (d*d);
  var B = (-2*a*d*d) - (2*a);
  var C = (a*a) + (a*a*d*d) - (l*l);

  x1 = (-1*B + Math.sqrt(B*B - 4*A*C)) / (2*A);
  x2 = (-1*B - Math.sqrt(B*B - 4*A*C)) / (2*A);

  if(c == 1) {
    if(step == 1) { result = Math.min(x1, x2); }
    else { result = Math.max(x1, x2); }
  }
  else {
    if(step == 1) { result = Math.max(x1, x2); }
    else { result = Math.min(x1, x2); }
  }
  return result;
}
function arrow_y(x, a, b, d) { // 화살표 왼쪽 좌표
  var result;
  result = d * x + b - a*d;
  return result;
}
function showMenu(x, y) {
  cntxtmenu.style.display = 'block';
  cntxtmenu.style.position = "absolute";
  cntxtmenu.style.top = y + "px";
  cntxtmenu.style.left = x + "px";
}
function hideMenu() {
  console.log("hide menu");
  event.stopPropagation();
  cntxtmenu.style.display = 'none';
}
$(function() {
  cntxtmenu = document.getElementById("context-menus");
  modify_BS = document.getElementById("modify_BS");
  modify_1_itemFP = document.getElementById("modify_1_itemFP");
  modify_n_itemFP = document.getElementById("modify_n_itemFP");
  modify_Proj = document.getElementById("modify_Proj");
  modify_SO = document.getElementById("modify_SO");
  modify_Agg = document.getElementById("modify_Agg");
  modify_HJ = document.getElementById("modify_HJ");
  modify_BJ = document.getElementById("modify_BJ");
  modify_TR = document.getElementById("modify_TR");

  $("#modeChanger").click(function(){
      if(drag_flag == 0) {
        drag_flag = 1;
        $('.div-content').draggable({disabled: false});
      }
      else {
        drag_flag = 0;
        $('.div-content').draggable({disabled: true});
      }
  })
  $(document).on("click",".linkline",function(){
    console.log("delete line");
    var id = $(event.target).attr('id');
    console.log(id);

    deleteLine(id);

    var p = this.parentElement;
    p.removeChild(this);
  })
  $('div').bind({
    contextmenu : function(event) {
      var divclass = $(event.target).attr('class');
      if(divclass == "div-content")
      {
        event.stopPropagation();
        event.preventDefault();
        var id = $(event.target).attr('id');

        cntxtmenuID = id;

        var divx = divE1.offset().left;
        var divy = divE1.offset().top;

        showMenu((divx+80), (divy+80));
      }
    },
    mousedown : function(event) {
      var divclass = $(event.target).attr('class');
      startid = $(event.target).attr('id');

      //if(divclass == "div-content") {
        event.stopPropagation();
        divE1 = $(event.target);

        //var divclass = $(event.target).attr('class');
        var divx = divE1.offset().left;
        var divy = divE1.offset().top;

        // console.log("*** M Down *** ");
        // console.log("X : " + divx);
        // console.log("Y : " + divy);

        divwid1 = divE1.width();
        divhei1 = divE1.height();
        if(drag_flag == 1) {}
        else {
          x1 = divx-marginX;
          y1 = divy-marginY;
        }
      //}
    },
    mouseup : function(event) {
      event.stopPropagation();

      if(cntxtmenu.style.display == 'block') {
        hideMenu();
      }

      divE2 = $(event.target);
      var localKey;
      var output, arr, arr2;
      destid = $(event.target).attr('id');
      var divx = divE2.offset().left -9;
      var divy = divE2.offset().top -11;

      divwid2 = divE2.width();
      divhei2 = divE2.height();

      localStorage.removeItem("loglevel:webpack-dev-server");

      for(var i=0; i<localStorage.length; i++) {
        output = localStorage.getItem(i);
        arr = JSON.parse(output);
        if(arr[0] == destid) {
          localKey = i;
        }
      }

      output = localStorage.getItem(localKey);
      arr = JSON.parse(output);
      if(arr == null) {}    // box가 아닌 다른 공간에 mouseup 할 때
      else {                // box에 mouseup 할 때
        var arr2 = [arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7], arr[8], arr[9], arr[10], divx, divy, arr[13], arr[14], arr[15]];
        localStorage.setItem(localKey, JSON.stringify(arr2));
      }

      if(drag_flag == 1) {  // drag 상태
        clearLine();
        createLine();
      }

      else {  // create or draw line 상태
        x2=divx-marginX;
        y2=divy-marginY;
        if(destid == "arrowbg" || destid =="app" || destid == "content" || destid == "aside" || destid == "header" || destid == startid) { }  // do nothing
        else {
          addLine_reload(startid, destid);
        }
      }
    }
  });
});
export default {
  data() {
    return {
      temp_para : [],

      // temp_para[0] : next 목적지 IP
      // temp_para[1] : next 목적지 Port
      
      test : [],
      schemaArray : [],
      taskListArray : [],
      svrArray : [],
      svrConfig : serverConfig,
      svrAddr : '',
      columnArray : [],
      jobId : '',
      linecnt : '',
      mode : "CREATE",
      task_type : '',
      task_id : '',
      input_schema : '',
      output_schema : '',
      listening_port : '',
      ec_id : '',
      output_type : '',
      heartbeat_task : '',
      heartbeat_job : '',
      config : '',
      add_jobSchema : '',
      taskcnt : '',
      localcnt : '',
      div_pos_x : '0',
      div_pos_y : '0',
      tempID : 0,
      openWin : '',
      position_x : '',
      position_y : '',
      taskID : '',
      temptask_id : '',
      temptask_name : '',
      tempinput_schema : '',
      templistening_port : '',
      tempec_id : '',
      tempoutput_type : '',
      tempconfig : '',
      tempheartbeat_task : '',
      tempheartbeat_job : '',
      tempoutput_schema : '',
      tempposition_x : '',
      tempposition_y : '',
      templinkto : '',
      templinkfrom : '',

      tempParent : '',
      parent : [],
      parentAttrs : [],
      myAttrs : [],
      tempArr : [],

      AggQuery : `Select * From `,
      SelectQuery : 'Select * ',
      FromQuery : 'From ',
      GBQuery : '',
      OBQuery: '',
      HAVQuery : '',

      AggTOTALQueryData : [],
      AggGENERALQueryData : [],
      AggGBQueryData : [],
      AggHAVQueryData : [],
      AggSELECTQueryData : [],
      AggOBQueryData : [],

      parentOutSchemaName : '',
      tempArr2 : [],
      val_InputQueueSize : 10,
      val_WindowType : 1,
      val_WindowSize : 60,
      val_NumberofPane : 6,
      val_SlidingSize : 10,

      des_InputQueueSize : '',
      des_WindowType : '',
      des_WindowSize : '',
      des_NumberofPane : '',
      des_SlidingSize : '',
      aggOBItems : [],
      HJfromID1 : '',
      HJfromID2 : '',
      HJfromID : [],
      HJQueryData : [],
      BJQueryData : [],
      TRData : [],
      SOServer : '',
      SOPort : '',
      SOInputQueueSize : 1,
      jobEXEresult : '',
      AggConfigTrans : [],
      number: 0,
      tweenedNumber: 0,
      testnumber : 0,

      temp_res : 10,
      dist_tasks : '',
      sieve1OP : [],
      sieve2OP : []
    }
  },
  created() {
    this.svrAddr = this.svrConfig.hostserver;
    var id = this.getParameterByName('jobID');
    this.jobId = id;
    this.loadTask(id);
    localStorage.clear();
    this.loadSvr();
    // this.loadSvrPost();
    this.loadSchema();
    this.loadTaskList();
  },
  computed: {
    animatedNumber: function() {
      return this.tweenedNumber.toFixed(0);
    }
  },
  watch: {
    number: function(newValue) {
      TweenLite.to(this.$data, 0.5, { tweenedNumber: newValue });
    }
  },
  methods : {
    func_test() {
      console.log("func test")
      this.temp_para.push(1)
      console.log(this.temp_para)
    },
    jobDistTotal() {
      console.log("jobDistTotal : ", this.jobId);
      var res;
      var _this = this;
      new Promise(function(resolve, reject){
        _this.setDistTasks();
        // _this.jobDistTotal_SetTasks();
        setTimeout(function() {
          resolve(1);
        }, 200);
      })
      .then(function(result) {
        _this.jobDistribute();
        // _this.jobDistTotal_DivTasksAndDist();
        return ;
      });
    },
    setDistTasks() {
      console.log("set Dist Tasks")
      var params = { job_id : this.jobId }
      var api = "http://" + this.svrAddr + ":3000/users/job_tasks";
      axios.post(api, params)
      .then( response => {
        this.dist_tasks = response.data
        console.log("Dist Tasks : ", this.dist_tasks)
      })
      .catch( response => {})
    },
    jobDistTotal_SetTasks() {
      var params = { job_id : this.jobId }
      var api = "http://" + this.svrAddr + ":3000/users/job_tasks";
      axios.post(api, params)
      .then( response => {
        this.temp_res = response.data;
        console.log("jobDist SetTasks : ", this.temp_res)
      })
      .catch( response => {})
    },
    jobDistTotal_DivTasksAndDist() {
      this.sieve1OP = [];
      this.sieve2OP = [];
      for(var i=0; i<this.temp_res.length; i++) {
        console.log(i + " : " + this.temp_res[i].program_type)
        var temp = returnArray(this.temp_res[i].id);
        if(this.temp_res[i].program_type == 3) {
          this.sieve1OP.push(temp);
        }
        else {
          this.sieve2OP.push(temp);
        }
      }
      if(this.sieve1OP.length == 0) {  // sieve2 연산자로만 이루어진 job
        this.jobDistribute();
      }
      else if(this.sieve2OP.length == 0) {   // sieve1 연산자로만 이루어진 job
        this.task2xml();
      }
      else {    // sieve1 연산자와 sieve2 연산자가 혼합되어진 job
        this.jobDistTotal_MixedTasksDist(function(svrAddr, s1tasks, s2tasks) {
          var params = {
            tasks : s1tasks,
            s2tasks : s2tasks
          }

          var api = "http://" + svrAddr + ":3000/users/job_tasks/xml2";
          axios.post(api, params)
          .then( response => {
            console.log(response.data);
            return response.data.success;
          })
          .then( response => {
            if(response == 1) {
              alert("***** JOB 배포 완료 *****");
            }
          })
          .catch( response => {});
        })
      }
    },
    jobDistTotal_MixedTasksDist(cb) {
      console.log(this.jobId);
      this.jobDistShowingStart();
      var params = { id : this.jobId }
      var api = "http://" + this.svrAddr + ":3000/users/jobs/distribute";
      axios.post(api, params)
      .then( response => {
        return response.data.success;
      })
      .then( response => {
        this.jobDistShowingEnd();
        cb(this.svrAddr, this.sieve1OP, this.sieve2OP);
      })
      .catch( response => {});
    },
    TRqueryAdd(message) {
      console.log("Transaction");
      this.TRData = message;
      console.log(this.TRData);
    },
    jobRUN() {
      console.log("funcDrafg");
      console.log(this.jobId);

      var params = {
        id : this.jobId
      }

      var api = "http://" + this.svrAddr + ":3000/users/jobs/run";

      axios.post(api, params)
      .then( response => {
        return response.data.success;
      })
      .then( response => {
        if(response == 1) {
          alert("***** JOB 실행 완료 *****");
        }
        else {
          alert("2");
        }
      })
      .catch( response => {});

    },
    jobDistribute() {
      console.log(this.jobId);
      this.jobDistShowingStart();
      var params = { id : this.jobId }
      var api = "http://" + this.svrAddr + ":3000/users/jobs/distribute";
      axios.post(api, params)
      .then( response => {
        console.log("success : ", response.data.success)
        this.jobDistShowingEnd();
        alert("***** JOB 배포 완료 *****");
      })
      .catch( response => {});
    },
    task2xml() {
      console.log("TASK 2 XML");
      // event.stopPropagation();
      // event.preventDefault();
      this.jobDistShowingStart();
      console.log("local length :: " + localStorage.length);
      var tasks = [];
      var count = 0;
      var output, arr;
      localStorage.removeItem("loglevel:webpack-dev-server");

      for(var i=0; i<localStorage.length; i++) {
        output = localStorage.getItem(i);
        arr = JSON.parse(output);

        if(arr[1] == this.jobId) {   // 현재 job ID와 같은 놈들을 고름 -> 향후 table 분리 검토
          tasks[count] = arr;
          count++;
        }
      }
      console.log(tasks);
      var params = {
        tasks : tasks,
        s2tasks : ''
      }
      var api = "http://" + this.svrAddr + ":3000/users/job_tasks/xml2";
      axios.post(api, params)
      .then( response => {
        console.log(response.data);
        return response.data.success;
      })
      .then( response => {
        if(response == 1) {
          this.jobDistShowingEnd();
          alert("***** JOB 배포 완료 *****");
        }
        else { alert("2"); }
      })
      .catch( response => {});
    },
    BJqueryAdd(message) {
      console.log("BJ Query Add");
      this.BJQueryData = message;
      console.log(this.BJQueryData);
    },
    HJqueryAdd(message) {
      console.log("HJ Query Add");
      this.HJQueryData = message;
    },
    makeOBItemList(message) {
      console.log("make OB Lists");
      console.log(message);

      this.aggOBItems = message;
    },
    abcd2() {
      console.log("abcd2");
      console.log(this.AggQuery);
    },
    aggQueryInput(message) {
      this.AggQuery = this.AggQuery + message;
    },
    aggQueryAddGB(message) {
      this.FromQuery = "From " + this.parentOutSchemaName + ' ';
      this.GBQuery = "Group By ";
      this.AggGBQueryData = [];
      var temp;
      for(var i=0; i<message.length; i++) {
        var data = new Object();
        data.GBIndex = message[i][0];
        data.name = message[i][1].name;
        this.AggGBQueryData.push(data);

        if(i == message.length-1) { temp=message[i][1].name; }
        else { temp = message[i][1].name + ', '; }
        this.GBQuery = this.GBQuery + temp;
      }
      this.makeAggQuery();
      this.makeAggTOTALQueryData();
      console.log("taskID : " + this.taskID);
    },
    aggQueryAddHAV(message) {
      this.FromQuery = "From " + this.parentOutSchemaName + ' ';
      this.HAVQuery = "Having ";
      this.AggHAVQueryData = [];
      var temp;
      this.AggHAVQueryData = message;

      for(var i=0; i<message.length; i++) {
        if(message[i].div == 0) {
          temp =
          message[i].typeName + '(' + message[i].distinctName + message[i].attrs.name + ')' + message[i].relOPName + message[i].constant + ' ';
        }
        else {
          temp = message[i].typeName + ' ' ;
        }
        this.HAVQuery = this.HAVQuery + temp;
      }

      this.makeAggQuery();
      this.makeAggTOTALQueryData();
    },
    aggQueryAddSelect(message) {
      console.log("Message");
      console.log(message);
      this.FromQuery = "From " + this.parentOutSchemaName + ' ';
      this.SelectQuery = "Select ";
      this.AggSELECTQueryData = [];
      var temp;
      this.AggSELECTQueryData = message;
      for(var i=0; i<message.length; i++) {
        if(i == message.length-1) {
          temp = message[i].opName + "(" + message[i].distinctName + message[i].AttrName + ') ';
        }
        else {
          temp = message[i].opName + "(" + message[i].distinctName + message[i].AttrName + '), ';
        }
        this.SelectQuery = this.SelectQuery + temp;
      }
      this.makeAggQuery();
      this.makeAggTOTALQueryData();
    },
    aggQueryAddOB(message) {
      console.log("OB Message");
      console.log(message);

      this.FromQuery = "From " + this.parentOutSchemaName + ' ';
      this.OBQuery = "Order By ";
      this.AggOBQueryData = [];
      var temp;
      this.AggOBQueryData = message;

      for(var i=0; i<message.length; i++) {
        if(i == message.length-1) {
          temp = message[i].itemName + ' ' + message[i].orderName + ' ';
        }
        else {
          temp = message[i].itemName + ' ' + message[i].orderName + ', ';
        }
        this.OBQuery = this.OBQuery + temp;
      }
      this.makeAggQuery();
      this.makeAggTOTALQueryData();
    },
    makeAggQuery() {
      this.AggQuery = this.SelectQuery + this.FromQuery + this.GBQuery + this.HAVQuery + this.OBQuery;
    },
    makeAggTOTALQueryData() {
      var AggGeneral = new Object();
      AggGeneral = {
        inQSize : this.val_InputQueueSize,
        winType : this.val_WindowType,
        winSize : this.val_WindowSize,
        numPane : this.val_NumberofPane,
        sldSize : this.val_SlidingSize,
        aggQuery : this.AggQuery
      }

      this.AggTOTALQueryData[0] = AggGeneral;
      this.AggTOTALQueryData[1] = this.AggGBQueryData;
      this.AggTOTALQueryData[2] = this.AggHAVQueryData;
      this.AggTOTALQueryData[3] = this.AggSELECTQueryData;
      this.AggTOTALQueryData[4] = this.AggOBQueryData;
    },
    projAddAttr() {
      var s = document.getElementById("projection_sel_box");
      var data = s.options[s.selectedIndex].value;

      for(var i=0; i<this.parentAttrs.length; i++) {
        console.log(this.parentAttrs[i].id);
        if(data == this.parentAttrs[i].id) {
          this.myAttrs.push(this.parentAttrs[i]); // showing
          this.tempArr.push(i-1);   // to sieve1
        }
      }
    },
    abcd() {
      this.jobId = 6;
      this.task_id = 3;
      this.input_schema = 53;
      this.listening_port = 1000;
      this.ec_id = 278;
      this.output_type = 0;
      this.config = 1;
      this.heartbeat_task = 0;
      this.heartbeat_job = 0;
      this.output_schema = 54;

      this.addTask_reload();
    },
    addTask_reload(){
      console.log("add task reload")
      var id = this.jobId;
      var _this = this;

      console.log("id : ", id)
      new Promise(function(resolve, reject){
        _this.addTask();
        setTimeout(function() {
          resolve(1);
        }, 200);
      })
      .then(function(result) {
        _this.clearBG();
        return result + 10;
      })
      .then(function(result) {
        _this.loadTask(id);
        return ;
      });

    },
    addTask() {
      var params = {
        job_id: this.jobId,
        task_id : this.task_id,
        listening_port : this.listening_port,
        ec_id : this.ec_id,
        position_x : 0,
        position_y : 0,
        linkto : null,
        linkfrom : null
      }

      var api = "http://" + this.svrAddr + ":3000/users/job_tasks/add";

      axios.post(api, params)
      .then( response => {})
      .catch( response => { console.log(response) } );
    },
    deleteTask_reload() {
      console.log("D Reload");

      var id = this.jobId;

      console.log("id : " + id);
      console.log("id : " + cntxtmenuID);
      var _this = this;
      new Promise(function(resolve, reject){
        _this.deleteTask(cntxtmenuID);
        setTimeout(function() {
          resolve(1);
        }, 200);
      })
      .then(function(result) {
        _this.clearBG();
        return result + 10;
      })
      .then(function(result) {
        _this.loadTask(id);
        return ;
      });
    },
    deleteTask(index) {
      console.log("DEL TASK");

      for(var i=0; i<lineArray.length; i++) {
        if(lineArray[i].from_id == index || lineArray[i].to_id == index) {
          console.log(i + " : " + lineArray[i].id);
          deleteLine(lineArray[i].id);
        }
      }

      var params = { id : index }

      var api = "http://" + this.svrAddr + ":3000/users/job_tasks/delete";

      axios.post(api, params)
      .then( response => {})
      .catch( response => { console.log(response) } );
    },
    boxPositionSave() {
      console.log("final save");
      var id = this.jobId;
      var _this = this;
      new Promise(function(resolve, reject){
        _this.updateTask();
        setTimeout(function() {
          resolve(1);
        }, 200);
      })
      .then(function(result) {
        _this.clearBG();
        return result + 10;
      })
      .then(function(result) {
        _this.loadTask(id);
        return ;
      });
    },
    make_parameter(index) {
      console.log("make parameter  : ", index);
      var config_data = new Object();
      this.tempconfig = ''

      var send_ip = this.which_ip(this.temp_para[0])
      config_data = {
        send_ec : this.temp_para[0],    // 목적지 ec
        send_ip : send_ip,              // 목적지 IP
        send_port : this.temp_para[1],  // 목적지 Port
      }

      switch(index) {
        case 0 :    // basestream
          config_data.send_speed = this.temp_para[2]
          var str = JSON.stringify(config_data);
          break;
        case 1 :    // Socket output
          config_data.out_type = this.temp_para[2];
          if(this.temp_para[2] == 1) {
            config_data.db = this.temp_para[3];
            config_data.table = this.temp_para[4];
          }
          if(this.temp_para[2] == 2) {
            config_data.filename = this.temp_para[3];
          }
          var str = JSON.stringify(config_data);
          break;
        case 2 :    // projection
          var str = this.tempArr.join('	');
          break;
        case 4 :    // aggregation
          var str = JSON.stringify(this.AggTOTALQueryData);
          break;
        case 6 :    // transaction
          var str = JSON.stringify(this.TRData);
          break;
        case 12 :   // binary join
          var str = JSON.stringify(this.BJQueryData);
          break;
        case 31 :   // half join
          var str = JSON.stringify(this.HJQueryData);
          break;
        case 33 :   // 1 item fp
          config_data.agg_type = this.temp_para[2];
          config_data.agg_unit = this.temp_para[3];
          config_data.min_support = this.temp_para[4];
          config_data.max_support = this.temp_para[5];

          var str = JSON.stringify(config_data);
          break;
        case 34 :   // n item fp
          config_data.n_item = this.temp_para[2];
          config_data.agg_type = this.temp_para[3];
          config_data.agg_unit = this.temp_para[4];
          config_data.min_support = this.temp_para[5];
          config_data.max_support = this.temp_para[6];

          var str = JSON.stringify(config_data);
          break;

        case 35 :   // Transactionizer
          var str = JSON.stringify(config_data);
          break;

      }
      this.tempconfig = JSON.stringify(str);
      console.log(this.tempconfig);
    },
    
    loadModifyData(index) {
      var _this = this;
      var parent;

      new Promise(function(resolve, reject){
        _this.findParent(index);
        setTimeout(function() {
          resolve(1);
        }, 200);
      })
      .then(function(result) {
        if(_this.parent.length == 1) {
          _this.parentOutSchema(1, _this.parent);
        }
        // for Half-Join
        else if(_this.parent.length == 2) {
          _this.parentOutSchema(2, _this.parent);
        }
        return;
      });

      var output, arr;
      arr = returnArray(index);

      this.temptask_id = arr[2];
      this.tempinput_schema = arr[3];
      this.templistening_port = arr[4];
      this.tempec_id = arr[5];
      this.tempoutput_type = arr[6];
      this.tempconfig = arr[7];
      this.tempheartbeat_task = arr[8];
      this.tempheartbeat_job = arr[9];
      this.tempoutput_schema = arr[10];
      this.tempposition_x = arr[11];
      this.tempposition_y = arr[12];
      this.templinkto = arr[13];
      this.templinkfrom = arr[14];
      this.temptask_name = arr[15];

      var temp = JSON.parse(this.tempconfig)

      this.temp_para[0] = temp.send_ec;
      this.temp_para[1] = temp.send_port;

      switch(arr[2]) {
        case 0 :    // basestream
          this.bs_send_ip = temp.send_ip;
          this.temp_para[2] = temp.send_speed;
          break;

        case 1 :    // socket output
          this.temp_para[2] = temp.out_type
          
          if(temp.out_type == 1) {
            this.temp_para[3] = temp.db
            this.temp_para[4] = temp.table
          }
          if(temp.out_type == 2) {
            this.temp_para[3] = temp.filename
          }
          break;

        case 4 :
          // var temp = JSON.parse(this.tempconfig);
          this.val_InputQueueSize = temp[0].inQSize;
          this.val_WindowType = temp[0].winType;
          this.val_WindowSize = temp[0].winSize;
          this.val_NumberofPane = temp[0].numPane;
          this.val_SlidingSize = temp[0].sldSize;
          this.AggQuery = temp[0].aggQuery;
          this.AggConfigTrans = JSON.parse(arr[7]);
          break;
        
        case 33 :         // fp_1_item
          this.temp_para[2] = temp.agg_type;
          this.temp_para[3] = temp.agg_unit;
          this.temp_para[4] = temp.min_support;
          this.temp_para[5] = temp.max_support;
          break;
        
        case 34 :         // fp_N_item
          this.temp_para[2] = temp.n_item
          this.temp_para[3] = temp.agg_type;
          this.temp_para[4] = temp.agg_unit;
          this.temp_para[5] = temp.min_support;
          this.temp_para[6] = temp.max_support;
          break;

        case 35 :         // Transactionizer
          break;
      }
    },
    updateModifyTask() {
      // console.log("updateModifyTask : ", this.tempconfig);
      localStorage.removeItem("loglevel:webpack-dev-server");   // localstorage update를 위해 삭제

      var params = {
        id : this.taskID,
        task_id : this.temptask_id,
        input_schema_id : this.tempinput_schema,
        listening_port : this.templistening_port,
        ec_id : this.tempec_id,
        output_type : this.tempoutput_type,
        config : this.tempconfig,
        heartbeat_task_id : this.tempheartbeat_task,
        heartbeat_job_id : this.tempheartbeat_job,
        output_schema_id : this.tempoutput_schema,
        position_x : this.tempposition_x - marginX,
        position_y : this.tempposition_y - marginY,
        linkto : this.templinkto,
        linkfrom : this.templinkfrom
      }

      var api = "http://" + this.svrAddr + ":3000/users/job_tasks/update";

      axios.post(api, params)
      .then( response => {})
      .catch( response => {});

      this.hideModifyMenu();      // modify 창 닫기
    },
    showModifyMenu(index, op_type) {
      console.log("op type222 : ", op_type)
      switch(op_type) {
        case 0:  modify_menu = modify_BS; break;
        case 1:  modify_menu = modify_SO; break;
        case 2:  modify_menu = modify_Proj; break;
        case 4:  modify_menu = modify_Agg; break;
        case 6:  modify_menu = modify_TR; break;
        case 12:  modify_menu = modify_BJ; break;
        case 31:  modify_menu = modify_HJ; break;
        case 33:  modify_menu = modify_1_itemFP; break;
        case 34:  modify_menu = modify_n_itemFP; break;
        case 35:  modify_menu = modify_TR; break;
        default : modify_menu = modify_BS; break;
       }

      modify_menu.style.display = 'block';
      modify_menu.style.position = "absolute";
      modify_menu.style.top = 0 + "px";
      modify_menu.style.left = 0 + "px";

      this.loadModifyData(index);
    },
    openTaskModify() {
      var output, arr, op_type;
      this.taskID = cntxtmenuID;

      console.log("open Task");

      arr = returnArray(cntxtmenuID);
      op_type = arr[2];
      console.log("op type : ", op_type)
      this.showModifyMenu(cntxtmenuID, op_type);
    },
    modifyTask_reload() {
      console.log("modifyTask called : ", this.temptask_id)
      var id = this.jobId;
      var _this = this;
      new Promise(function(resolve, reject){
        _this.make_parameter(_this.temptask_id)       // config에 parameter 값 json 형태로 저장
        _this.updateModifyTask();
        setTimeout(function() {
          resolve(1);
        }, 200);
      })
      .then(function(result) {
        _this.clearBG();
        return result + 10;
      })
      .then(function(result) {
        _this.loadTask(id);
        return ;
      });
    },
    which_ip(index) {
      for(var i in this.svrArray) {
        // console.log("this : ", i)
        // if(this.svrArray[i].id == this.bs_send_ec) {
        if(this.svrArray[i].id == index) {
          var ip = this.svrArray[i].ip_address
        }
      }
      console.log("which ip : ", ip)
      return ip
    },
    findParent(index) {
      // console.log("1. FIND Parent");
      var params = { to_id : index }
      var api = "http://" + this.svrAddr + ":3000/users/task_lines/findparent";
      axios.post(api, params)
      .then( response => {
        this.parent = response.data;
        // console.log(this.parent);

      })
      .catch( response => { console.log(response) } );
    },
    abcdparent() {
      console.log("abcdPaeee");
      console.log(this.parentAttrs);

    },
    HJparentSchema(index, fromID, data) {
      var temp = new Object();

      // temp.fromID = fromID;
      // temp.data = data;

      this.parentAttrs[index] = fromID;

      // this.parentAttrs.push(fromID);

    },
    parentOutSchema(cnt, index) {
      // console.log("3. Parent Schema --");
      this.parentAttrs = [];    // initialize

      if(cnt == 1) {
        var arr;
        arr = returnArray(index[0].from_id);
        this.parentOutSchemaName = this.returnSchemaName(arr[10]);

        var api = "http://" + this.svrAddr + ":3000/users/cell_columns";
        var params = { schema_id: arr[10] }
        axios.post(api, params)
        .then( response => {
          this.parentAttrs = response.data;
         })
        .catch( response => { console.log(response) } );
      }
      else {
        this.HJfromID = new Array();
        this.HJfromID[0] = index[0].from_id;
        this.HJfromID[1] = index[1].from_id;

        for(var i=0; i<2; i++) {
          var arr;
          arr = returnArray(index[i].from_id);
          this.parentOutSchemaName = this.returnSchemaName(arr[10]);

          var api = "http://" + this.svrAddr + ":3000/users/cell_columns";
          var params = { schema_id: arr[10] }
          axios.post(api, params)
          .then( response => {
            this.parentAttrs.push(response.data);
           })
          .catch( response => { console.log(response) } );
        }
      }
    },
    returnSchemaName(index) {
      for(var i=0; i<this.schemaArray.length; i++) {
        if(this.schemaArray[i].id == index) {
          return this.schemaArray[i].name;
        }
      }
    },
    hideModifyMenu() {
      modify_menu.style.display = 'none';
    },
    deleteTaskAll(index) {
      var params = {
        job_id: this.jobId
      }
      var api = "http://" + this.svrAddr + ":3000/users/job_tasks/delete_all";

      axios.post(api, params)
      .then( response => { })
      .catch( response => { console.log(response) } );

      var api2 = "http://" + this.svrAddr + ":3000/users/task_lines/delete_joblines";

      axios.post(api2, params)
      .then( response => { })
      .catch( response => { console.log(response) } );
    },
    updateTask() {
      localStorage.removeItem("loglevel:webpack-dev-server");
      for(var i=0; i<localStorage.length; i++) {
        var output = localStorage.getItem(i);
        var arr = JSON.parse(output);
        var params = {
          id : arr[0],
          job_id : arr[1],
          task_id : arr[2],
          input_schema_id : arr[3],
          listening_port : arr[4],
          ec_id : arr[5],
          output_type : arr[6],
          config : arr[7],
          heartbeat_task_id : arr[8],
          heartbeat_job_id : arr[9],
          output_schema_id : arr[10],
          position_x : arr[11],
          position_y : arr[12],
          linkto : arr[13],
          linkfrom : arr[14]
        }

        var api = "http://" + this.svrAddr + ":3000/users/job_tasks/update";

        axios.post(api, params)
        .then( response => { })
        .catch( response => { console.log(response) } );
      }
      alert("Saved");
    },
    loadLine(index) {
      console.log("load Line calle2");
      var params = { job_id: index }
      var api = "http://" + this.svrAddr + ":3000/users/task_lines";

      axios.post(api, params)
      .then( response => {
        lineArray = response.data;
        createLine();
      })
      .catch( response => { console.log(response) } );
    },
    loadTask(index) {
      var params = { job_id: index }
      var api = "http://" + this.svrAddr + ":3000/users/job_tasks";
      axios.post(api, params)
      .then( response => {
        taskArray = response.data;
        this.taskcnt = response.data.length;
        this.initDIV();
        loadLine(index);
      })
      .catch( response => { console.log(response) } );
    },
    initDIV() {
      for(var i=0; i<this.taskcnt; i++) {
        this.createDIV(i);
        this.localize(
          taskArray[i].id,
          taskArray[i].job_id,
          taskArray[i].task_id,
          taskArray[i].input_schema_id,
          taskArray[i].listening_port,
          taskArray[i].ec_id,
          taskArray[i].output_type,
          taskArray[i].config,
          taskArray[i].heartbeat_task_id,
          taskArray[i].heartbeat_job_id,
          taskArray[i].output_schema_id,
          taskArray[i].position_x,
          taskArray[i].position_y,
          taskArray[i].linkto,
          taskArray[i].linkfrom,
          taskArray[i].name
        );
      }
      this.taskcnt = 0;
    },
    createDIV(index) {
      var obj = document.getElementById("content");
      var newDIV = document.createElement("div");
      var id = taskArray[index].id;
      var name = taskArray[index].name;
      var ec = taskArray[index].ec_id;
      var port = taskArray[index].listening_port;
      var marginLeft = taskArray[index].position_x;
      var marginTop = taskArray[index].position_y;
      var position = "margin-left : " + marginLeft + "px; margin-top : " + marginTop + "px;";

      newDIV.setAttribute("id", id);
      newDIV.setAttribute('class', "div-content");
      newDIV.innerHTML = "<table> <tr> <p class='no-drag'>" + name + "</p> </tr> <tr> <p class='no-drag2'> TID : " + id + "</p> </tr> <tr> <p class='no-drag2'> Eng : " + ec + "</p> </tr> <tr> <p class='no-drag2'> Port :" + port + "</p> </tr> </table>";
      newDIV.setAttribute("style", position);
      obj.appendChild(newDIV);
    },
    createDIV2() {
      var id = this.tempID;
      var name;
      var obj = document.getElementById("content");
      var newDIV = document.createElement("div");

      newDIV.setAttribute("id", id);
      newDIV.setAttribute('class', "div-content");
      newDIV.innerHTML = "<table> <tr> <p class='no-drag'> " + name + " </p> </tr> <tr> <p class='no-drag2'> " + id + " </p> </tr> </table>";

      obj.appendChild(newDIV);
      this.localize(
        id,
        this.jobId,
        this.task_id,
        this.input_schema,
        this.listening_port,
        this.ec_id,
        this.output_type,
        this.config,
        this.heartbeat_task,
        this.heartbeat_job,
        this.output_schema,
        0,
        0,
        null,
        null,
        name
      );
      this.tempID += 1;
      this.clearInput();

    },
    localize(id, job_id, task_id, input_schema, listening_port, ec_id, output_type, config, heartbeat_task, heartbeat_job, output_schema, position_x, position_y, linkto, linkfrom, name) {
      localStorage.removeItem("loglevel:webpack-dev-server");
      var localLength = localStorage.length;
      var arr = [id, job_id, task_id, input_schema, listening_port, ec_id, output_type, config, heartbeat_task, heartbeat_job, output_schema, position_x, position_y, linkto, linkfrom, name];
      localStorage.setItem(localLength, JSON.stringify(arr));
      this.localcnt = localLength + 1;
    },
    clearInput() {
      this.task_id = '',
      this.input_schema = '',
      this.listening_port = '',
      this.ec_id = '',
      this.output_type = '',
      this.config = '',
      this.heartbeat_task = '',
      this.heartbeat_job = '',
      this.output_schema = ''
    },
    changeMode() {
      if(this.mode == "DRAG" ) {
        this.mode = "CREATE / LINK";
      }
      else {
        this.mode = "DRAG";
      }
    },
    clearBG() {
      console.log("clear BG");
      var select = 0;
      var content = document.getElementById("content");
      var arrowbg = document.getElementById("arrowbg");

      var contentNodeLength = content.childNodes.length;
      var arrowbgNodeLength = arrowbg.childNodes.length;

      while(arrowbgNodeLength > 0) {
        arrowbg.removeChild(arrowbg.firstChild);
        arrowbgNodeLength = arrowbgNodeLength - 1;
      }

      while(contentNodeLength > 0) {
        if(content.childNodes[select].id == "arrowbg") {
          select += 1;
        }
        else {
          content.removeChild(content.childNodes[select]);
        }
        contentNodeLength = contentNodeLength - 1;
      }
      localStorage.clear();
    },
    loadTaskList() {
      var api = "http://" + this.svrAddr + ":3000/users/tasks";
      axios.get(api)
      .then(response => { this.taskListArray = response.data; })
      .catch(err => { console.log(err); });
    },
    loadSvr() {
      var api = "http://" + this.svrAddr + ":3000/users/engine_computer";
      axios.get(api)
      .then(response => {
        this.svrArray = response.data;
      })
      .catch(err => { console.log(err); });
    },
    loadSvrPost() {
      var api = "http://" + this.svrAddr + ":3000/users/engine_computer2";
      var params = {
        searchTarget : this.currentuserid
      }

      axios
      .post(api, params)
      .then( response => {
        // this.pageArray = response.data;
        this.svrArray = response.data
      })
      .catch( response => { console.log(response) } );
    },
    loadSchema() {
      var api = "http://" + this.svrAddr + ":3000/users/cell_schemas";

      axios.get(api)
      .then(response => { this.schemaArray = response.data; })
      .catch(err => { console.log(err); });
    },
    getParameterByName(name) {
      name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
      var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
      results = regex.exec(location.search);
      return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    },
    tabChange(index, tab_content) {
      var x = document.getElementsByClassName("aggtabcontent");
      var i;
      for (i = 0; i < x.length; i++) {
          x[i].style.display = 'none';
      }
      document.getElementById(tab_content).style.display = 'block';
      var x = document.getElementsByClassName("tabmenu");
      var i;
      for (i = 0; i < x.length; i++) {
          x[i].className = 'tabmenu';
      }
      document.getElementById(index).className = 'tabmenu active';
    },
    jobDistShowingStart() {
      console.log("Fun TEST START");
      var backHeight = $(document).height(); //뒷 배경의 상하 폭
      var backWidth = window.document.body.clientWidth; //뒷 배경의 좌우 폭
      var opacity = 0.1;
      var color = "red";
      var backGroundCover = `<div id='loadingBack' style="width:${backWidth}px; height:${backHeight}px; opacity:${opacity};"></div>`; //뒷 배경을 감쌀 커버
      var loadingBarImage = `<div id='loadingBar' style="border:1px solid grey; padding : 5px; background:white; color:${color}; font-size:16px; font-weight:700;"> JOB Distributing </div>`;

      $('body').append(backGroundCover).append(loadingBarImage);
      $('#loadingBack').show();
      $('#loadingBar').show();

      setInterval(function() {
        setTimeout(function() {
          $('#loadingBar').hide();
        },750);

        setTimeout(function() {
          $('#loadingBar').show();
        },1500);
      }, 1500)

    },
    jobDistShowingEnd() {
      $('#loadingBack, #loadingBar').hide();
      $('#loadingBack, #loadingBar').remove();
    }
  },
  components: {
    'AggGeneral' : AggGeneral,
    'AggGroupBy' : AggGroupBy,
    'AggHaving' : AggHaving,
    'AggSelect' : AggSelect,
    'AggOrderBy' : AggOrderBy,
    'HalfJoin' : HalfJoin,
    'BinaryJoin' : BinaryJoin,
    'Transaction' : Transaction
  }
}
</script>

<style src="./css/total.css"></style>
