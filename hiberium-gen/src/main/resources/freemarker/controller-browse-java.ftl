
    @RequestMapping(value = "/${concept_apipath}/all", method = RequestMethod.GET)
    public ResponseEntity<List<${concept_name}>> getAll${concept_name}(
        ) throws Exception {
        List<${concept_name}> ${concept_varname}List = repository.findAll();
        log.info("SELECT ${concept_name} all");
        return ResponseEntity.ok().body(${concept_varname}List);
    }

    @RequestMapping(value = "/${concept_apipath}/page/{page}", method = RequestMethod.GET)
    public ResponseEntity<List<${concept_name}>> getAll${concept_name}ByPage(
        @PathVariable(value = "page") Integer pageNum,
        @RequestParam(value = "perPage", defaultValue = "10") Integer perPage
        ) throws Exception {

        if(pageNum == null || pageNum <= 0)
            pageNum = 1;
        Integer offset = (pageNum - 1) * perPage;

        List<${concept_name}> ${concept_varname}List = repository.findAllByLimitAndOffset(perPage, offset);
        log.info("SELECT ${concept_name} WHERE pageNum={}", pageNum);
        return ResponseEntity.ok().body(${concept_varname}List);
    }