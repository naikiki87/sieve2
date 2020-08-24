<template>
  <div style="position : absolute; top : 10px; width : 1000px; background:white;">
    <div id="content">
      <svg id="arrowbg"> </svg>
    </div>
    <div id="header">
      <table valign="middle" align="left" style="height:130px;margin:auto; width:250px; padding-top : 5px; background:;">
        <tr>
          <td v-model="JOB_ID" style="font-size:22px; font-weight:700;"> JOB : {{ JOB_ID }} </td>
        </tr>
      </table>
      <table id="TDHeader_table" style=" margin : auto; width:97%; border:0px">
        <tr>
          <td> <button class="JobDetailHeaderbutton2" v-on:click="jobDistTotal2"> DISPATCH </button> </td>
        </tr>
        <br>
        <tr>
          <td> <button class="JobDetailHeaderbutton2" v-on:click="jobRUN"> RUN </button> </td>
        </tr>
      </table>
    </div>
    <div id="rightaside">
      <table class="TDrightTable" style="height:30px; width:100%; border:0px; color:white;">
        <tr>
          <td style="background:black; font-size:14px; font-weight:700;"> Add Task </td>
        </tr>
      </table>
      <br>
      <table class="TDrightTable">
        <tr>
          <td style="width : 80px;" class="add_title"> Task </td>
          <td>
            <select style="width : 99%;" v-model="task_id">
              <option v-for="(item, index) in taskListArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
            </select>
          </td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> Edge </td>
          <td>
            <select style="width : 99%;" v-model="ec_id">
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{ item.ip_address }} </option>
            </select>
          </td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> Port </td>
          <td> <input style="width : 95%;" type="text" v-model="listening_port"></td>
        </tr>
      </table>
      <br>
      <br>
      <button style="height:40px; width:200px; font-size:14px;" class="JobDetailHeaderbutton" v-on:click="addTask_reload"> ADD </button>
      <button style="height:40px; width:200px; font-size:14px;" class="JobDetailHeaderbutton" v-on:click="func_test"> TEST </button>
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
            <select style="width : 99%;" v-model="tempec_id" disabled>
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 수행 포트 </td>
          <td> <input style="width : 95%;" type="text" v-model="templistening_port" disabled></td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 목적 IP </td>
          <td> <input style="width : 95%;" type="text" v-model="tempdest_ip" disabled></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적 Port </td>
          <td> <input style="width : 95%;" type="text" v-model="tempdest_port" disabled></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 입력 스키마 </td>
          <td>
            <select style="width : 99%;" v-model="tempinput_schema">
              <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
            </select>
          </td>
        </tr>

        <!-- <tr>
          <td style="width : 80px;" class="add_title"> 전송 속도 </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[0]"></td>
        </tr> -->
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
            <select style="width : 99%;" v-model="tempec_id" disabled>
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 수행 포트 </td>
          <td> <input style="width : 95%;" type="text" v-model="templistening_port" disabled></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적지 IP </td>
          <td> <input style="width : 95%;" type="text" v-model="tempdest_ip"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적 Port </td>
          <td> <input style="width : 95%;" type="text" v-model="tempdest_port"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> Output 방식 </td>
          <td>
            <select id="taskSelect" style="width : 99%; border:1px solid;" v-model="temp_para[0]">
              <option value = "0"> socket </option>
              <option value = "1"> db </option>
              <option value = "2"> file </option>
            </select>
          </td>
        </tr>
        <tr v-if="temp_para[0] === '1'">
          <td style="background:lightgrey;"> DB </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[1]"></td>
        </tr>
        <tr v-if="temp_para[0] === '1'">
          <td style="background:lightgrey;"> Table </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[2]"></td>
        </tr>

        <tr v-if="temp_para[0] === '2'">
          <td style="background:lightgrey;"> 파일명 </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[1]"></td>
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
            <select style="width : 99%;" v-model="tempec_id" disabled>
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 수행 포트 </td>
          <td> <input style="width : 95%;" type="text" v-model="templistening_port" disabled></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적지 IP </td>
          <td> <input style="width : 95%;" type="text" v-model="tempdest_ip"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적 Port </td>
          <td> <input style="width : 95%;" type="text" v-model="tempdest_port"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 집계 기준 </td>
          <td>
            <select id="taskSelect" style="width : 99%; border:1px solid;" v-model="temp_para[0]">
              <option value = "0"> Time </option>
              <option value = "1"> Tuple </option>
            </select>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 집계 단위 </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[1]"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> Min Support </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[2]"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> Max Support </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[3]"></td>
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
            <select style="width : 99%;" v-model="tempec_id" disabled>
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 수행 포트 </td>
          <td> <input style="width : 95%;" type="text" v-model="templistening_port" disabled></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적지 IP </td>
          <td> <input style="width : 95%;" type="text" v-model="tempdest_ip" disabled></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적 Port </td>
          <td> <input style="width : 95%;" type="text" v-model="tempdest_port" disabled></td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> n item </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[0]"></td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 집계 기준 </td>
          <td>
            <select id="taskSelect" style="width : 99%; border:1px solid;" v-model="temp_para[1]">
              <option value = "0"> Time </option>
              <option value = "1"> Tuple </option>
            </select>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 집계 단위 </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[2]"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> Min Support </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[3]"></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> Max Support </td>
          <td> <input style="width : 95%;" type="text" v-model="temp_para[4]"></td>
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
            <select style="width : 99%;" v-model="tempec_id" disabled>
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 수행 포트 </td>
          <td> <input style="width : 95%;" type="text" v-model="templistening_port" disabled></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적지 IP </td>
          <td> <input style="width : 95%;" type="text" v-model="tempdest_ip" disabled></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적 Port </td>
          <td> <input style="width : 95%;" type="text" v-model="tempdest_port" disabled></td>
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
                  <option v-for="(item, index) in prev_attrs" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
                </select>
              </div>
            </div>
          </td>
          <td>
            <div class="proj_attr_btwbox">
              <button style="margin-top : 120px; width : 50px;" v-on:click="proj_add_attr"> add </button>
            </div>
          </td>
          <td>
            <div class="proj_attr_box">
              <div> <h3 class="proj_attr_title"> 선택 속성 </h3> </div>
              <div>
                <select class="proj_attr_list" id="projection_selected_box" size=4>
                  <option v-for="(item, index) in selected_attrs" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
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
    <div id="modify_RD_1">
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
            <select style="width : 99%;" v-model="tempec_id" disabled>
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 수행 포트 </td>
          <td> <input style="width : 95%;" type="text" v-model="templistening_port" disabled></td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 목적 IP </td>
          <td> <input style="width : 95%;" type="text" v-model="tempdest_ip" disabled></td>
        </tr>

        <tr>
          <td style="width : 80px;" class="add_title"> 목적 Port </td>
          <td> <input style="width : 95%;" type="text" v-model="tempdest_port" disabled></td>
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
          <td style="width : 80px;" class="add_title"> 출력 스키마 </td>
          <td>
            <select style="width : 99%;" v-model="tempoutput_schema">
              <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
            </select>
          </td>
        </tr>
      </table>
      <br> <br>
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
    <div id="modify_RT">
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
            <select style="width : 99%;" v-model="tempec_id" disabled>
              <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td style="width : 80px;" class="add_title"> 수행 포트 </td>
          <td> <input style="width : 95%;" type="text" v-model="templistening_port" disabled></td>
        </tr>
      </table>
      <br> <br>
      <button id="modifyButton" v-on:click="modifyTask_reload"> SAVE </button>
      <button id="modifyButton" v-on:click="hideModifyMenu"> CLOSE </button>
    </div>

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

var WIDTH = 90
var JOB_ID, USER_ID

var start_div_x, start_div_y, end_div_x, end_div_y
var start_class, end_class
var rt_check;
var startid, destid;
var x1, y1, x2, y2;
var divE1, divE2;
var marginX=0;
var marginY=0;
var cntxtmenu;
var modify_menu;
var modify_BS, modify_Proj, modify_SO, modify_Agg, modify_HJ, modify_BJ, modify_TR, modify_1_itemFP, modify_n_itemFP, modify_RD_1, modify_RT;
var cntxtmenuID;
var t;
var taskArray = [];
var lineArray = [];
var call = 0;
var taskcnt;
var box_move = 0;
var rel_lines;
var rel_tasks;
var selected_box;
var t_pos_x, t_pos_y

function get_iteminfo(index) {
  var res;
  var params = { id : index }
  var api = "http://" + serverADDR + ":3000/users/get_taskinfo";
  return axios.post(api, params)
}
function div_mode_change() {
  $('.div-content').draggable({disabled: false});         // drag 가능 상태로 변경
}
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
async function addLine_reload(start, end) {
  console.log("addLine_reload")
  await addLine(start, end)   // line 연결정보 db 저장
  await clearBG()
  await load_task_line(JOB_ID)
}
async function load_task_line(index) {
  var params = { job_id: index }
  var api = "http://" + serverADDR + ":3000/users/job_tasks";

  taskArray = (await axios.post(api, params)).data
  taskcnt = taskArray.length
  await initDIV()
  await loadLine(index)
  await div_mode_change()
}
function initDIV() {
  for(var i=0; i<taskcnt; i++) {
    createDIV(i);
  }
  taskcnt = 0;
}
async function addTask(task) {
  var api = "http://" + serverADDR + ":3000/users/job_tasks/add";
  await axios.post(api, task)
}
function createDIV(index) {      // box 생성
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
}
async function deleteTask(index) {
  console.log("DEL TASK");

  for(var i=0; i<lineArray.length; i++) {
    if(lineArray[i].from_id == index || lineArray[i].to_id == index) {
      console.log(i + " : " + lineArray[i].id);
      await deleteLine(lineArray[i].id);
    }
  }

  var params = { 
    id : index,
    user_id : USER_ID
  }
  var api = "http://" + serverADDR + ":3000/users/job_tasks/delete";
  await axios.post(api, params)

  var api = "http://" + serverADDR + ":3000/users/cell_schemas/del_taskschema";
  await axios.post(api, params)
}
async function loadLine(index) {
  var params = { job_id: index }
  var api = "http://" + serverADDR + ":3000/users/task_lines";

  lineArray = (await axios.post(api, params)).data
  await create_line()
}
function getParameterByName(name) {
  name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
  var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
  results = regex.exec(location.search);
  return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}
function clearLine() {    // canvas의 line을 모두 지움
  var arrowbg = document.getElementById("arrowbg");
  var arrowbgNodeLength = arrowbg.childNodes.length;

  while(arrowbgNodeLength > 0) {
    console.log("clearLine : ", arrowbg.firstChild)
    console.log("clearLine2 : ", arrowbg)
    console.log("clearLine3 : ", arrowbg.children)
    console.log("clearLine4 : ", arrowbg.children.length)
    arrowbg.removeChild(arrowbg.firstChild);
    arrowbgNodeLength = arrowbgNodeLength - 1;
  }
}
async function clear_rel_line(lines) {    // canvas의 related line 만 지우고 redraw
  var id, from_id, to_id;
  var start, end;
  var start_x, start_y, end_x, end_y;

  for(var i in lines) {
    id = lines[i].id
    for(var j=0; j<arrowbg.children.length; j++) {
      if(id == arrowbg.children[j].id) {
        arrowbg.removeChild(arrowbg.children[j])     // line delete
      }
    }
  }

  for(var i in lines) {
    id = lines[i].id
    from_id = lines[i].from_id
    to_id = lines[i].to_id

    start = (await get_iteminfo(from_id)).data[0]
    end = (await get_iteminfo(to_id)).data[0]

    start_x = start.position_x
    start_y = start.position_y
    end_x = end.position_x
    end_y = end.position_y

    drawLine(start_x, start_y, end_x, end_y, WIDTH, WIDTH, WIDTH, WIDTH, id);
  }
}
async function addLine(start, end) {    // line 정보를 db에 저장
  console.log("ADD LINE");

  var params = {
    job_id : JOB_ID,
    from_id : start,
    to_id : end
  }

  var api = "http://" + serverADDR + ":3000/users/task_lines/add";
  await axios.post(api, params)
  console.log("addline 2")
  api = "http://" + serverADDR + ":3000/users/update_dest_info";
  await axios.post(api, params)
}
async function create_line() {
  var output, arr;
  var start_x, start_y, end_x, end_y;
  var id;
  var from_id, to_id;
  var start, end

  for(var i=0; i<lineArray.length; i++) {
    id = lineArray[i].id;
    from_id = lineArray[i].from_id
    to_id = lineArray[i].to_id

    start = (await get_iteminfo(from_id)).data[0]
    end = (await get_iteminfo(to_id)).data[0]

    start_x = start.position_x
    start_y = start.position_y
    end_x = end.position_x
    end_y = end.position_y
    drawLine(start_x, start_y, end_x, end_y, WIDTH, WIDTH, WIDTH, WIDTH, id);
  }
}
async function deleteLine(index) {
  console.log("DEL LINE");

  var params = {
    id : index
  }

  var api = "http://" + serverADDR + ":3000/users/task_lines/delete"
  await axios.post(api, params)
  console.log("delete line from db complete")
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
  event.stopPropagation();
  cntxtmenu.style.display = 'none';
}
function box_position_update(id, pos_x, pos_y) {
  var params = {
    id : id,
    pos_x : pos_x,
    pos_y : pos_y
  }
  var api = "http://" + serverADDR + ":3000/users/update_task_pos";
  axios.post(api, params)
}
function clearBG() {
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
}
function over_check(x, y, id) {
  var tasks = document.getElementsByClassName("div-content")
  var left, right, top, bottom
  var ob_left, ob_right, ob_top, ob_bottom
  var res = 0

  ob_left = x
  ob_right = x + WIDTH
  ob_top = y
  ob_bottom = y + WIDTH

  for(var i =0; i<tasks.length; i++) {
    if(tasks[i].id == id) {
      continue
    }
    else {
      left = tasks[i].offsetLeft
      right = left + WIDTH
      top = tasks[i].offsetTop
      bottom = top + WIDTH
      if((ob_left < right) && (ob_left > left)) {
        if((ob_bottom > top) && (ob_bottom < bottom)) {
          res = tasks[i].id         // 겹치는 경우
        }
        else if((ob_top > top) && (ob_top < bottom)) {
          res = tasks[i].id         // 겹치는 경우
        }
      }
      else if((ob_right > left) && (ob_right <right)) {
        if((ob_bottom > top) && (ob_bottom < bottom)) {
          res = tasks[i].id         // 겹치는 경우
        }
        else if((ob_top > top) && (ob_top < bottom)) {
          res = tasks[i].id         // 겹치는 경우
        }
      }
    }
  }
  return res
}
function realtime_redraw(startid, lines, tasks, x, y) {
  var id, from_id, to_id
  var start_x, start_y, end_x, end_y

  for(var i in lines) {
    var lineid = lines[i].id
    for(var j=0; j<arrowbg.children.length; j++) {
      if(lineid == arrowbg.children[j].id) {
        arrowbg.removeChild(arrowbg.children[j])     // line delete
      }
    }
  }

  for(var i in lines) {
    id = lines[i].id
    from_id = lines[i].from_id
    to_id = lines[i].to_id

    if(startid == from_id) {
      start_x = x
      start_y = y
      for(var j in tasks) {
        if(to_id == tasks[j].id) {
          end_x = tasks[j].x
          end_y = tasks[j].y
        }
      }
    }

    else if(startid == to_id) {
      for(var j in tasks) {
        if(from_id == tasks[j].id) {
          start_x = tasks[j].x
          start_y = tasks[j].y
        }
      }
      end_x = x
      end_y = y
    }
    drawLine(start_x, start_y, end_x, end_y, WIDTH, WIDTH, WIDTH, WIDTH, id);
  }
}
async function get_JOB_ID() {
  var en_C = await getParameterByName('job');
  var d_add_add = en_C[0]+en_C[2]+en_C[4]+en_C[6]+en_C[8]
  var d_add = parseInt(d_add_add, 16) - 5555
  var real_add = en_C[9]+en_C[7]+en_C[5]+en_C[3]+en_C[1]
  JOB_ID = parseInt(real_add, 16) - d_add
  
  return JOB_ID
}
async function get_USER_ID() {
  USER_ID = await getParameterByName('case');
  
  return USER_ID
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
  modify_RD_1 = document.getElementById("modify_RD_1");
  modify_RT = document.getElementById("modify_RT");

  $(document).on("click",".linkline", async function(){
    console.log("delete line");
    var line = $(event.target)
    var id = $(event.target).attr('id');
    console.log("line : ", line, id)

    await deleteLine(id);         // db에서 해당 line 정보 삭제
    var p = this.parentElement;
    console.log("p : ", p)
    console.log("this : ", this)
    await p.removeChild(this);
    console.log("after : ", p)
  })
  $('div').bind({
    contextmenu : function(event) {
      divE2 = $(event.target);
      var dest_class = divE2[0].className
      try {
        if(dest_class.indexOf("div-content") !== -1) {
          event.stopPropagation();
          event.preventDefault();
          var id = $(event.target).attr('id');
          cntxtmenuID = id;
          var divx = divE1.offset().left;
          var divy = divE1.offset().top;
          showMenu((divx+80), (divy+80));
        }
      }
      catch(e) {}
    },
    mousedown : async function(event) {
      var divclass = $(event.target).attr('class');
      startid = $(event.target).attr('id');

      event.stopPropagation();
      divE1 = $(event.target);
      
      start_class = divE1[0].className
      try {
        if(start_class.indexOf("div-content") !== -1) {
          start_div_x = divE1.offset().left;
          start_div_y = divE1.offset().top;

          x1 = start_div_x-marginX;
          y1 = start_div_y-marginY;

          box_move = 1

          var params = { id : startid }
          var api = "http://" + serverADDR + ":3000/users/rel_lines";
          rel_lines = ''
          rel_lines = (await axios.post(api, params)).data
          rel_tasks = []
          var from_id, to_id

          for(var i=0; i<rel_lines.length; i++) {
            from_id = rel_lines[i].from_id
            to_id = rel_lines[i].to_id

            if(startid != from_id) {
              var temp = {
                id : from_id
              }
            }
            else {
              var temp = {
                id : to_id
              }
            }
            rel_tasks.push(temp)
          }

          for(var i=0; i<rel_tasks.length; i++) {
            var t_x = document.getElementById(rel_tasks[i].id).offsetLeft
            var t_y = document.getElementById(rel_tasks[i].id).offsetTop
            rel_tasks[i].x = t_x
            rel_tasks[i].y = t_y
          }

          selected_box = document.getElementById(startid)
          var temp_x = selected_box.offsetLeft
          var temp_y = selected_box.offsetTop
          realtime_redraw(startid, rel_lines, rel_tasks, temp_x, temp_y)
        }  
      }
      catch(e) {}   // do nothing
    },
    mousemove : async function(event) {
      if(box_move == 1) {
        try {
          realtime_redraw(startid, rel_lines, rel_tasks, selected_box.offsetLeft, selected_box.offsetTop)
        }
        catch(e) {}
      }
    },
    mouseup : async function(event) {
      event.stopPropagation();
      box_move = 0

      try {
        if(cntxtmenu.style.display == 'block') {    // modify 메뉴 가리기
          await hideMenu();
        }
      }
      catch(e) {}

      divE2 = $(event.target);
      destid = $(event.target).attr('id');
      end_class = divE2[0].className
      try {
        if(end_class.indexOf("div-content") !== -1) {
          var temp_x = divE2[0].offsetLeft
          var temp_y = divE2[0].offsetTop
          var link_id = await over_check(temp_x, temp_y, destid)    // 연결되는 task id

          if(link_id == 0) {     // 연결되는 놈이 없을 경우
            await box_position_update(destid, temp_x, temp_y)   // box 위치 db update
            // var params = { id : destid }
            // var api = "http://" + serverADDR + ":3000/users/rel_lines";
            // rel_lines = (await axios.post(api, params)).data
            // clear_rel_line(rel_lines)
          }
          else {              // 연결되는 놈이 있는 경우
            console.log("link exist : box link")
            box_position_update(destid, start_div_x, start_div_y)     // 기본 박스 원위치
            addLine_reload(destid, link_id)
          }
        }
      }
      catch(e) {}
    }
  });
});
export default {
  data() {
    return {
      prev_attrs : [],
      selected_attrs : [],
      temp_para : [],

      test : [],
      schemaArray : [],
      taskListArray : [],
      svrArray : [],
      svrConfig : serverConfig,
      svrAddr : '',
      columnArray : [],

      JOB_ID : '',
      USER_ID : '',
      mode : "CREATE",
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
      tempdest_ip : '',
      tempdest_port : '',

      tempParent : '',
      parent : [],
      parentAttrs : [],
      proj_selected_attr : [],
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

      temp_res : 10,
      dist_tasks : '',
      sieve1OP : [],
      sieve2OP : []
    }
  },
  async created() {
    this.svrAddr = this.svrConfig.hostserver;
    this.JOB_ID = await get_JOB_ID()
    this.USER_ID = await get_USER_ID()
    await load_task_line(JOB_ID)
    await this.loadSvr();
    await this.loadSchema();
    await this.loadTaskList();
    // this.RT_health_check()
  },
  computed: {
  },
  watch: {
  },
  methods : {
    async func_test() {
      console.log("func test")
      console.log("user id : ", USER_ID, this.USER_ID)
      var showtime = new Date()
      var curtime = showtime.getSeconds() + ':' + showtime.getMilliseconds()
      console.log("test : ", curtime)

    },
    RT_health_check() {
      console.log("RT_health_check")
      // var repeat = setInterval(this.health_check, 5000)
      rt_check = setInterval(this.health_check, 5000)
    },
    async health_check() {
      var ret = 0
      for(var i=0; i<taskArray.length; i++) {
        var params = {
          task_id : taskArray[i].id
        }
        var api = "http://" + this.svrAddr + ":3000/users/healthcheck";
        var res = await axios.post(api, params)
        // console.log(taskArray[i].id, ' : ', res.data.alive)
        var target = document.getElementById(taskArray[i].id)
        if(res.data.alive == 1) {
          // target.style.background = "#11fe2e"
          target.style.background = "#3af42c"
          ret = ret + 1
        }
        else if(res.data.alive == 0) {
          target.style.background = "#ffffff"
        }
      }

      // ret = 1
      return ret
    },
    async jobDistTotal2() {
      console.log("jobDistTotal : ", JOB_ID);

      var params = { job_id : JOB_ID }
      var api = "http://" + this.svrAddr + ":3000/users/job_tasks";
      this.dist_tasks = (await axios.post(api, params)).data

      await this.jobDistribute();
    },
    async jobDistTotal() {
      console.log("jobDistTotal : ", JOB_ID);
      await this.setDistTasks();
      await this.jobDistribute();
    },
    setDistTasks() {
      console.log("set Dist Tasks")
      var params = { job_id : JOB_ID }
      var api = "http://" + this.svrAddr + ":3000/users/job_tasks";
      axios.post(api, params)
      .then( response => {
        this.dist_tasks = response.data
        console.log("Dist Tasks : ", this.dist_tasks)
      })
      .catch( response => {})
    },
    jobDistTotal_SetTasks() {
      var params = { job_id : JOB_ID }
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
      console.log(JOB_ID);
      this.jobDistShowingStart();
      var params = { id : JOB_ID }
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
    async jobRUN() {
      console.log("jobRUN : ", JOB_ID);
      var params = {
        id : JOB_ID,
        user_id : USER_ID
      }
      var api = "http://" + this.svrAddr + ":3000/users/jobs/run";
      var success = (await axios.post(api, params)).data.success
      if(success == true) {
        if((await this.health_check()) == taskArray.length) {
        // if((await this.health_check()) == 1) {
          // alert("***** JOB 실행 완료 *****");
          console.log("JOB RUN COMPLETE")
        }
      }
    },
    jobDistribute() {
      console.log(JOB_ID);
      this.jobDistShowingStart();
      var params = { 
        id : JOB_ID,
        user_id : USER_ID
      }
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

        if(arr[1] == JOB_ID) {   // 현재 job ID와 같은 놈들을 고름 -> 향후 table 분리 검토
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
    proj_add_attr() {
      console.log("proj att attrs")
      var s = document.getElementById("projection_sel_box");
      var data = s.options[s.selectedIndex].value;
      var temp_selected = [];

      for(var i=0; i<this.prev_attrs.length; i++) {
        console.log(i, ' : ', this.prev_attrs[i].id)
        if(data == this.prev_attrs[i].id) {
          this.selected_attrs.push(this.prev_attrs[i])
          this.prev_attrs.splice(i, 1)                  // 선택된 attr을 temp attr에서 삭제
        }
      }
    },
    async addTask_reload(){
      console.log("add task reload")
      var id = JOB_ID;
      var port_using = await this.port_using_check(this.ec_id, this.listening_port)
      if(port_using == 1) {
        alert(this.listening_port, " : Port Duplicate")
      }
      else {
        var task = {
          job_id: JOB_ID,
          task_id : this.task_id,
          listening_port : this.listening_port,
          ec_id : this.ec_id,
          position_x : 0,
          position_y : 0,
          linkto : null,
          linkfrom : null,
          dest_ip : "",
          dest_port : 0
        }
        await addTask(task)
        await clearBG()
        await load_task_line(id)
        await this.clearInput()
      }
    },
    async port_using_check(ec_id, lis_port) {
      console.log("port using check : ", ec_id, lis_port)
      var res;
      var params = {
        ec_id : ec_id,
        lis_port : lis_port
      }
      var api = "http://" + this.svrAddr + ":3000/users/port_use_check";

      return (await axios.post(api, params)).data.use
    },
    async deleteTask_reload() {
      await deleteTask(cntxtmenuID)
      await clearBG()
      await load_task_line(JOB_ID)
    },
    make_parameter(index) {               // data update를 위한 parameter 데이터 세팅
      console.log("make parameter  : ", index);
      var config_data = new Object();
      this.tempconfig = ''

      // var send_ip = this.which_ip(this.temp_para[0])
      // config_data = {
      //   send_ec : this.temp_para[0],    // 목적지 ec
      //   send_ip : send_ip,              // 목적지 IP
      //   send_port : this.temp_para[1],  // 목적지 Port
      // }

      switch(index) {
        case 0 :    // basestream
          config_data = {
            send_speed : this.temp_para[0]
          }
          var str = JSON.stringify(config_data);

          console.log("0000 : ", str)
          break;
        case 1 :    // Socket output
          config_data.out_type = this.temp_para[0];
          if(this.temp_para[0] == 1) {
            config_data.db = this.temp_para[1];
            config_data.table = this.temp_para[2];
          }
          if(this.temp_para[0] == 2) {
            config_data.filename = this.temp_para[1];
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
          config_data.agg_type = this.temp_para[0];
          config_data.agg_unit = this.temp_para[1];
          config_data.min_support = this.temp_para[2];
          config_data.max_support = this.temp_para[3];

          var str = JSON.stringify(config_data);
          break;
        case 34 :   // n item fp
          config_data.n_item = this.temp_para[0];
          config_data.agg_type = this.temp_para[1];
          config_data.agg_unit = this.temp_para[2];
          config_data.min_support = this.temp_para[3];
          config_data.max_support = this.temp_para[4];

          var str = JSON.stringify(config_data);
          break;

        case 35 :   // Transactionizer
          var str = JSON.stringify(config_data);
          break;

      }
      this.tempconfig = JSON.stringify(str);
      console.log("temp config : ", this.tempconfig);
    },
    async loadModifyData(index) {           // modify 창의 data load
      console.log("loadModifyData : ", index)
      var _this = this;
      var output, arr;
      var params = {
        id : index
      }

      var api = "http://" + this.svrAddr + ":3000/users/get_prev_attrs";
      var prev_attrs = (await axios.post(api, params)).data

      var api = "http://" + this.svrAddr + ":3000/users/get_taskinfo";
      axios.post(api, params)
      .then( response => {
        arr = response.data[0]
        this.temptask_id = arr.task_id;
        this.tempinput_schema = arr.input_schema_id;
        this.templistening_port = arr.listening_port;
        this.tempec_id = arr.ec_id;
        this.tempoutput_type = arr.output_type;
        this.tempconfig = arr.config;
        this.tempheartbeat_task = arr.heartbeat_task_id;
        this.tempheartbeat_job = arr.heartbeat_job_id;
        this.tempoutput_schema = arr.output_schema_id;
        this.tempposition_x = arr.position_x;
        this.tempposition_y = arr.position_y;
        this.templinkto = arr.linkto;
        this.templinkfrom = arr.linkfrom;
        this.temptask_name = arr.name;
        this.tempdest_ip = arr.dest_ip;
        this.tempdest_port = arr.dest_port;

        var temp = JSON.parse(this.tempconfig)

        switch(this.temptask_id) {
          case 0 :    // basestream
            this.temp_para[0] = temp.send_speed
            break;

          case 1 :    // socket output
            this.temp_para[0] = temp.out_type
            
            if(temp.out_type == 1) {
              this.temp_para[1] = temp.db
              this.temp_para[2] = temp.table
            }
            if(temp.out_type == 2) {
              this.temp_para[1] = temp.filename
            }
            break;

          case 2 :      // projection
            this.prev_attrs = prev_attrs
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
            this.temp_para[0] = temp.agg_type;
            this.temp_para[1] = temp.agg_unit;
            this.temp_para[2] = temp.min_support;
            this.temp_para[3] = temp.max_support;
            break;
          
          case 34 :         // fp_N_item
            this.temp_para[0] = temp.n_item
            this.temp_para[1] = temp.agg_type;
            this.temp_para[2] = temp.agg_unit;
            this.temp_para[3] = temp.min_support;
            this.temp_para[4] = temp.max_support;
            break;

          case 35 :         // Transactionizer
            break;
          
          case 36 :         // receive_data_1
            break;
        }


      })
      .catch( response => {});
    },
    updateModifyTask() {
      console.log("updateModifyTask : ", this.tempconfig);
      // localStorage.removeItem("loglevel:webpack-dev-server");   // localstorage update를 위해 삭제

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
        linkfrom : this.templinkfrom,
        dest_ip : this.tempdest_ip,
        dest_port : this.tempdest_port
      }

      console.log("params : ", params)

      var api = "http://" + this.svrAddr + ":3000/users/job_tasks/update";

      axios.post(api, params)
      .then( response => {})
      .catch( response => {});

      this.hideModifyMenu();      // modify 창 닫기
    },
    showModifyMenu(index, op_type) {
      // console.log("op type222 : ", op_type)
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
        case 36:  modify_menu = modify_RD_1; break;
        case 37:  modify_menu = modify_RT; break;
        default : modify_menu = modify_BS; break;
       }

      modify_menu.style.display = 'block';
      modify_menu.style.position = "absolute";
      modify_menu.style.top = 0 + "px";
      modify_menu.style.left = 0 + "px";

      this.loadModifyData(index);
    },
    async openTaskModify() {      // open task modify window
      this.taskID = cntxtmenuID;
      var res = (await get_iteminfo(cntxtmenuID)).data[0]
      var op_type = res.task_id
      this.showModifyMenu(cntxtmenuID, op_type);
    },
    async modifyTask_reload() {
      console.log("modifyTask_reload : ", this.temptask_id)

      switch(this.temptask_id) {
        case 0 :      // basestream
          this.tempoutput_schema = this.tempinput_schema  
          break

        case 2 :      // projection
          this.tempoutput_schema = await this.make_new_schema(this.taskID, this.selected_attrs) 
          break
      }

      await this.make_parameter(this.temptask_id)
      await this.updateModifyTask()
      await clearBG()
      await load_task_line(JOB_ID)
    },
    async make_new_schema(tid, attrs) {
      console.log("make new schema")
      var params = {
        tid : tid,
        attrs : attrs
      }
      var api = "http://" + this.svrAddr + ":3000/users/make_new_schema";
      return (await axios.post(api, params)).data.new_sid
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
    },
    async loadSvr() {
      var api = "http://" + this.svrAddr + ":3000/users/engine_computer";
      this.svrArray = (await axios.get(api)).data
    },
    async loadSchema() {
      var api = "http://" + this.svrAddr + ":3000/users/cell_schemas";
      this.schemaArray = (await axios.get(api)).data
    },
    async loadTaskList() {
      var api = "http://" + this.svrAddr + ":3000/users/tasks";
      this.taskListArray = (await axios.get(api)).data
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
