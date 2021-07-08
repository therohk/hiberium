
    @RequestMapping(value = "/${concept_apipath}/{id}", method = RequestMethod.GET)
    public ResponseEntity<${concept_name}> get${concept_name}ById(
            @PathVariable(value = "id") Integer ${concept_varname}Id
            ) throws Exception {

        ${concept_name} ${concept_varname} = repository.getOne(${concept_varname}Id);
        if (${concept_varname} == null)
            throw new Exception("${concept_name} Id " + ${concept_varname}Id + " not found");
        log.info("SELECT ${concept_name} where id=" + ${concept_varname}Id);
        return ResponseEntity.ok().body(${concept_varname});
    }