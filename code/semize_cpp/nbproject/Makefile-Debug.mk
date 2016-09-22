#
# Generated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
GREP=grep
NM=nm
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc
CCC=g++
CXX=g++
FC=gfortran
AS=as

# Macros
CND_PLATFORM=GNU-Linux-x86
CND_DLIB_EXT=so
CND_CONF=Debug
CND_DISTDIR=dist
CND_BUILDDIR=build

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=${CND_BUILDDIR}/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/DiscreteMatrix.o \
	${OBJECTDIR}/DiscreteVectorBitMagician.o \
	${OBJECTDIR}/DiscreteVectorStorageInfo.o \
	${OBJECTDIR}/DiscreteVectorValueGetterInfo.o \
	${OBJECTDIR}/DiscreteVectorValueSetterInfo.o \
	${OBJECTDIR}/GreedyCoverCalculator.o \
	${OBJECTDIR}/RandomizationRoundRunner.o \
	${OBJECTDIR}/Semize.o \
	${OBJECTDIR}/SemizeInitializer.o \
	${OBJECTDIR}/Stochastic.o \
	${OBJECTDIR}/main.o


# C Compiler Flags
CFLAGS=

# CC Compiler Flags
CCFLAGS=-msse4.1 -std=gnu++0x -march=native -O3 -fopenmp
CXXFLAGS=-msse4.1 -std=gnu++0x -march=native -O3 -fopenmp

# Fortran Compiler Flags
FFLAGS=

# Assembler Flags
ASFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-${CND_CONF}.mk ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/semize

${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/semize: ${OBJECTFILES}
	${MKDIR} -p ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}
	${LINK.cc} -o ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/semize ${OBJECTFILES} ${LDLIBSOPTIONS}

${OBJECTDIR}/DiscreteMatrix.o: DiscreteMatrix.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/DiscreteMatrix.o DiscreteMatrix.cpp

${OBJECTDIR}/DiscreteVectorBitMagician.o: DiscreteVectorBitMagician.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/DiscreteVectorBitMagician.o DiscreteVectorBitMagician.cpp

${OBJECTDIR}/DiscreteVectorStorageInfo.o: DiscreteVectorStorageInfo.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/DiscreteVectorStorageInfo.o DiscreteVectorStorageInfo.cpp

${OBJECTDIR}/DiscreteVectorValueGetterInfo.o: DiscreteVectorValueGetterInfo.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/DiscreteVectorValueGetterInfo.o DiscreteVectorValueGetterInfo.cpp

${OBJECTDIR}/DiscreteVectorValueSetterInfo.o: DiscreteVectorValueSetterInfo.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/DiscreteVectorValueSetterInfo.o DiscreteVectorValueSetterInfo.cpp

${OBJECTDIR}/GreedyCoverCalculator.o: GreedyCoverCalculator.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/GreedyCoverCalculator.o GreedyCoverCalculator.cpp

${OBJECTDIR}/RandomizationRoundRunner.o: RandomizationRoundRunner.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/RandomizationRoundRunner.o RandomizationRoundRunner.cpp

${OBJECTDIR}/Semize.o: Semize.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/Semize.o Semize.cpp

${OBJECTDIR}/SemizeInitializer.o: SemizeInitializer.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/SemizeInitializer.o SemizeInitializer.cpp

${OBJECTDIR}/Stochastic.o: Stochastic.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/Stochastic.o Stochastic.cpp

${OBJECTDIR}/main.o: main.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/main.o main.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r ${CND_BUILDDIR}/${CND_CONF}
	${RM} ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/semize

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
