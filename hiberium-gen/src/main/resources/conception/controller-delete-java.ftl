
    @RequestMapping(value = "/${concept_apipath}/{id}", method = RequestMethod.DELETE)
    public ${concept_name} delete${concept_name}(
            @PathVariable(value = "id") Integer ${concept_varname}Id
            ) throws Exception {

        ${concept_name} ${concept_varname} = service.findByPrimaryKey(${concept_varname}Id);
        repository.deleteById(${concept_varname}Id);
        return ${concept_varname};
    }

<#--
    //soft delete not implemented
    @RequestMapping(value = "/${concept_apipath}/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> delete${concept_name}(
            @PathVariable(value = "id") Integer ${concept_varname}Id
            ) throws Exception {

        ${concept_name} ${concept_varname} = new ${concept_name}();
        ${concept_varname}.primaryKey(${concept_varname}Id);
        ${concept_varname}.handleFieldsForDelete();

        service.handle${concept_name}InsertOrUpdate(${concept_varname}, "B");
        return ResponseEntity.ok(true);
    }
-->