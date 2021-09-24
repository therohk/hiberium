
    @RequestMapping(value = "/${concept_apipath}/{id}", method = RequestMethod.GET)
    public ResponseEntity<${concept_name}> get${concept_name}ById(
            @PathVariable(value = "id") Integer ${concept_varname}Id
            ) throws Exception {

        ${concept_name} ${concept_varname} = service.findByPrimaryKey(${concept_varname}Id);
        return ResponseEntity.ok().body(${concept_varname});
    }