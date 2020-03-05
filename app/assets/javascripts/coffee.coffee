
$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/loginPost'
    add: '/addPost'
    delete: '/deletePost'
    adddata: '/adddataPost'
    deletedata: '/deletedataPost'
    changedata: '/changedataPost'


  vm = ko.mapping.fromJS
    login: ''
    password: ''
    roleList: [{id: "1", name: "User"}, {id: "2", name: "Helper"}]
    selectedRole: ''
    hududList: [{id: "1", name: "Urganch shahar"}, {id: "2", name: "Urganch tuman"}, {id: "3", name: "Bog'ot tumani"},
      {id: "4", name: "Gurlan tumani"}, {id: "5", name: "Qo'shko'pir tumani"}, {id: "6", name: "Xonqa tumani"},
      {id: "7", name: "Xiva tumani"}, {id: "8", name: "Hazorasp tumani"}, {id: "9", name: "Shotov tumani"},
      {id: "10", name: "Yangiariq tumani"}, {id: "11", name: "Yangibozor tumani"}, {id: "12", name: "Xiva shahar"}]
    selectedHudud: ''
    rahbarList: [{id: "1", name: "Hokim"}, {id: "2", name: "Prokuror"}, {id: "3", name: "IIB boshligi"}, {id: "4", name: "DSI boshligi"}]
    selectedRahbar: ''
    date: ''
    text: ''
    valueList: [{id: "1", name: "Yangi"}, {id: "2", name: "Bajarilgan"}, {id: "3", name: "Bajarilmagan"}]
    selectedValue: ''


  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  vm.onSubmit = ->
    data =
      login: vm.login()
      password: vm.password()

    $.ajax
      url: apiUrl.send
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      toastr.success(response)

  vm.add = ->
    data =
      login: vm.login()
      password: vm.password()
      role: vm.selectedRole()
    $.ajax
      url: apiUrl.add
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      toastr.success(response)


  vm.delete = ->
    data =
      login: vm.login()
      password: vm.password()
      role: vm.selectedRole()
    $.ajax
      url: apiUrl.delete
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      toastr.success(response)

  vm.adddata = ->
    data =
      hudud: vm.selectedHudud()
      rahbar: vm.selectedRahbar()
      date: vm.date()
      text: vm.text()
      value: vm.selectedValue()
    $.ajax
      url: apiUrl.adddata
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      toastr.success(response)


  vm.deletedata = ->
    data =
      hudud: vm.selectedHudud()
      rahbar: vm.selectedRahbar()
      date: vm.date()
      text: vm.text()
      value: vm.selectedValue()
    $.ajax
      url: apiUrl.deletedata
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      toastr.success(response)

  vm.changedata = ->
    data =
      hudud: vm.selectedHudud()
      rahbar: vm.selectedRahbar()
      date: vm.date()
      text: vm.text()
      value: vm.selectedValue
    $.ajax
      url: apiUrl.delete
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      toastr.success(response)

  ko.applyBindings {vm}